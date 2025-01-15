#include "../include/logicalOperations/OrGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue OrGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Or operation requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateOrCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedOrFunc(arg1, arg2), 15);
    return result;
}

void OrGenerator::generateOrCode() {
    cachedOrFunc = compileCode<jboolean(*)(jboolean, jboolean)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::d0;   // Регистр для a
            auto bReg = asmjit::a64::d1;   // Регистр для b
            auto resultReg = asmjit::a64::d2;  // Регистр для результата

            // Преобразуем аргументы в целые числа для операции OR
            assembler.fcvtzs(asmjit::a64::x2, aReg);  // Преобразуем arg1 в целое число
            assembler.fcvtzs(asmjit::a64::x3, bReg);  // Преобразуем arg2 в целое число

            // Применяем побитовое ИЛИ
            assembler.orr(asmjit::a64::x2, asmjit::a64::x2, asmjit::a64::x3); // x2 = x2 | x3

            // Переносим результат обратно в d0
            assembler.mov(asmjit::a64::d0, asmjit::a64::x2);
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            // Преобразуем аргументы в целые числа
            assembler.cvtsd2si(asmjit::x86::rax, asmjit::x86::xmm0); // Преобразуем arg1 в целое
            assembler.cvtsd2si(asmjit::x86::rbx, asmjit::x86::xmm1); // Преобразуем arg2 в целое

            // Применяем побитовое ИЛИ
            assembler.or_(asmjit::x86::rax, asmjit::x86::rbx); // rax = rax | rbx

            // Переводим результат обратно в xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
