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

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateAndCode();
    });

    jvalue result;
    result.z = cachedAndFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;  // Правильная логика для AND
    return result;
}

void AndGenerator::generateAndCode() {
    cachedAndFunc = compileCode<jboolean(*)(jboolean, jboolean)>(
        [](ASMJIT_ASSEMBLER& assembler) {
    #ifdef __arm64__
            auto aReg = asmjit::a64::x0;  // Регистр для первого аргумента (arg1)
            auto bReg = asmjit::a64::x1;  // Регистр для второго аргумента (arg2)
            auto resultReg = asmjit::a64::x2;  // Регистр для результата

            // Логическая операция AND
            assembler.and_(resultReg, aReg, bReg);  // x2 = x0 & x1 (AND operation)

            // Переносим результат обратно в x0 (результат будет 0 или 1)
            assembler.mov(asmjit::a64::x0, resultReg);
            assembler.ret();
    #elif defined(__x86_64__)
            // Логическая операция AND
            assembler.test(asmjit::x86::rdi, asmjit::x86::rsi);  // rdi & rsi
            assembler.setnz(asmjit::x86::al);  // Устанавливаем результат в al (0 or 1)

            // Переводим результат в jboolean
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::al);  // Переводим результат в xmm0 (jboolean)
            assembler.ret();
    #endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
