#include "../include/logicalOperations/NotGenerator.h"

jvalue NotGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 1) {
        throw std::runtime_error("NOT operation requires exactly 1 argument");
    }

    jdouble arg = getArgAs<jdouble>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateNotCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedNotFunc(arg), 15);  // Округление с точностью до 15 знаков
    return result;
}

void NotGenerator::generateNotCode() {
    cachedNotFunc = compileCode<jdouble(*)(jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __x86_64__
            // Загружаем аргумент в xmm0
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::ptr(asmjit::x86::rdi));  // arg

            // Преобразуем в целочисленный тип
            assembler.cvtsd2si(asmjit::x86::rax, asmjit::x86::xmm0);

            // Выполняем побитовую операцию NOT
            assembler.not_(asmjit::x86::rax);

            // Возвращаем результат в xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax);

            assembler.ret();
#endif
        }
    );
}
