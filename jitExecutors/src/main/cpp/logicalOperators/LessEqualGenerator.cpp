#include "../include/logicalOperations/LessEqualGenerator.h"

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
    result.d = roundToPrecision(cachedLessEqualFunc(arg1, arg2), 15);  // Округление с точностью до 15 знаков
    return result;
}

void LessEqualGenerator::generateLessEqualCode() {
    cachedLessEqualFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __x86_64__
            // Загружаем аргументы в xmm0 и xmm1
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::ptr(asmjit::x86::rdi));  // arg1
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::ptr(asmjit::x86::rsi));  // arg2

            // Сравниваем значения
            assembler.ucomisd(asmjit::x86::xmm0, asmjit::x86::xmm1);
            assembler.setbe(asmjit::x86::al);  // Устанавливаем флаг, если arg1 <= arg2

            // Преобразуем флаг в число (0 или 1)
            assembler.movzx(asmjit::x86::rax, asmjit::x86::al);

            // Возвращаем результат в xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax);

            assembler.ret();
#endif
        }
    );
}
