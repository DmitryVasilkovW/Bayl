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
        throw std::runtime_error("OR operation requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateOrCode();
    });

    jvalue result;
    result.z = cachedOrFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void OrGenerator::generateOrCode() {
    cachedOrFunc = compileCode<jboolean(*)(jboolean, jboolean)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::x0;  // Регистр для первого аргумента (arg1)
            auto bReg = asmjit::a64::x1;  // Регистр для второго аргумента (arg2)
            auto resultReg = asmjit::a64::x2;  // Регистр для результата

            // Логическая операция OR (побитовое ИЛИ)
            assembler.orr(resultReg, aReg, bReg);  // x2 = x0 | x1 (OR операция)

            // Переносим результат обратно в регистр x0 для возврата
            assembler.mov(asmjit::a64::x0, resultReg);
            assembler.ret();
#elif defined(__x86_64__)
            // Логическая операция OR (побитовое ИЛИ)
            assembler.or_(asmjit::x86::rdi, asmjit::x86::rsi);  // rdi = rdi | rsi

            // Переносим результат обратно в xmm0
            assembler.movzx(asmjit::x86::rax, asmjit::x86::rdi); // Получаем 0 или 1
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax); // Переводим в xmm0
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
