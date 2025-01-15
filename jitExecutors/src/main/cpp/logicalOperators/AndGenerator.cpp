#include "../include/logicalOperations/AndGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue AndGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("AND operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateAndCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedAndFunc(arg1, arg2), 15);
    return result;
}

void AndGenerator::generateAndCode() {
    cachedAndFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::d0;   // Регистр для a
            auto bReg = asmjit::a64::d1;   // Регистр для b
            auto resultReg = asmjit::a64::d2;  // Регистр для результата

            // Преобразование значений в целочисленные типы (для побитовой операции)
            assembler.fcvtzs(asmjit::a64::x3, aReg);   // Преобразуем d0 в x3 (int)
            assembler.fcvtzs(asmjit::a64::x4, bReg);   // Преобразуем d1 в x4 (int)

            // Побитовая операция AND
            assembler.and_(asmjit::a64::x5, asmjit::a64::x3, asmjit::a64::x4);  // x5 = x3 & x4

            // Результат возвращаем в d2
            assembler.fcvtzsd(resultReg, asmjit::a64::x5);  // Преобразуем x5 обратно в double (d2)
            assembler.mov(asmjit::a64::d0, resultReg);  // Переносим результат в d0
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            // Для x86_64 используем xmm0 и xmm1
            assembler.movsd(asmjit::x86::xmm2, asmjit::x86::xmm0); // Сохраняем значение xmm0 в xmm2
            assembler.movsd(asmjit::x86::xmm3, asmjit::x86::xmm1); // Сохраняем значение xmm1 в xmm3

            // Преобразуем значения в целые числа
            assembler.cvtsd2si(asmjit::x86::eax, asmjit::x86::xmm2);  // xmm2 -> eax
            assembler.cvtsd2si(asmjit::x86::ebx, asmjit::x86::xmm3);  // xmm3 -> ebx

            // Побитовая операция AND
            assembler.and_(asmjit::x86::eax, asmjit::x86::ebx);  // eax = eax & ebx

            // Преобразуем результат обратно в double
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::eax);  // eax -> xmm0

            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
