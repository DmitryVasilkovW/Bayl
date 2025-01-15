#include "../include/logicalOperations/LessEqualGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue LessEqualGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("LessEqual operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateLessEqualCode();
    });

    jvalue result;
    result.z = cachedLessEqualFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;  // Возвращаем JNI_TRUE или JNI_FALSE
    return result;
}

void LessEqualGenerator::generateLessEqualCode() {
    cachedLessEqualFunc = compileCode<jboolean(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
    #ifdef __arm64__
            auto aReg = asmjit::a64::d0;  // Регистр для первого аргумента (arg1)
            auto bReg = asmjit::a64::d1;  // Регистр для второго аргумента (arg2)
            auto resultReg = asmjit::a64::x0;  // Регистр для логического результата

            // Сравниваем значения
            assembler.fcmp(aReg, bReg);  // Сравнение arg1 и arg2

            // Устанавливаем логический результат (0 или 1)
            assembler.cset(resultReg, asmjit::a64::Condition::kLe);  // Если arg1 <= arg2, результат = 1

            // Переносим логический результат обратно в x0
            assembler.ret();
    #elif defined(__x86_64__)
            // Загружаем аргументы для сравнения
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::ptr(asmjit::x86::rdi));  // arg1
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::ptr(asmjit::x86::rsi));  // arg2

            // Сравниваем значения
            assembler.ucomisd(asmjit::x86::xmm0, asmjit::x86::xmm1);  // Устанавливаем флаги для сравнения

            // Устанавливаем флаг AL, если arg1 <= arg2
            assembler.setbe(asmjit::x86::al);

            // Преобразуем флаг в число (0 или 1)
            assembler.movzx(asmjit::x86::rax, asmjit::x86::al);

            // Завершаем выполнение
            assembler.ret();
    #endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
