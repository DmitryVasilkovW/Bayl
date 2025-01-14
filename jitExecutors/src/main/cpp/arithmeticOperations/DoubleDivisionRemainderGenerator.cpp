#include "../include/arithmeticOperations/DoubleDivisionRemainderGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue DoubleDivisionRemainderGenerator::generate(
    JNIEnv* env, 
    jobject obj, 
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Division remainder requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateDivisionRemainderCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedDivRemFunc(arg1, arg2), 15);
    return result;
}

void DoubleDivisionRemainderGenerator::generateDivisionRemainderCode() {
    asmjit::CodeHolder code;
    code.init(asmjit::Environment::host());

    cachedDivRemFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto dividend = asmjit::a64::d0;
            auto divisor = asmjit::a64::d1;
            auto quotient = asmjit::a64::d2;
            auto temp = asmjit::a64::d3;

            // Загружаем значения в регистры
            assembler.fmov(dividend, dividend);
            assembler.fmov(divisor, divisor);

            // Вычисление частного: quotient = floor(dividend / divisor)
            assembler.fdiv(quotient, dividend, divisor);
            assembler.frintz(quotient, quotient); // Округление вниз до целого

            // Вычисление остатка: dividend = dividend - (quotient * divisor)
            assembler.fmul(temp, quotient, divisor);
            assembler.fsub(dividend, dividend, temp);

            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            // Загружаем значения в регистры
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::xmm0); // dividend
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::xmm1); // divisor

            // Копируем делимое в xmm2
            assembler.movapd(asmjit::x86::xmm2, asmjit::x86::xmm0);

            // Вычисление частного: xmm2 = xmm0 / xmm1
            assembler.divsd(asmjit::x86::xmm2, asmjit::x86::xmm1);

            // Обрезаем дробную часть частного: xmm2 = floor(xmm2)
            assembler.roundsd(asmjit::x86::xmm2, asmjit::x86::xmm2, 1); // 1 = округление вниз

            // Вычисление остатка: xmm0 = xmm0 - (xmm2 * xmm1)
            assembler.mulsd(asmjit::x86::xmm2, asmjit::x86::xmm1);
            assembler.subsd(asmjit::x86::xmm0, asmjit::x86::xmm2);

            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
