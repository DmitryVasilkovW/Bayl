#include "../include/logicalOperations/AndGenerator.h"

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
    result.d = roundToPrecision(cachedAndFunc(arg1, arg2), 15);  // Округление с точностью до 15 знаков
    return result;
}

void AndGenerator::generateAndCode() {
    cachedAndFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __x86_64__
            // Загружаем аргументы в xmm0 и xmm1
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::ptr(asmjit::x86::rdi));  // arg1
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::ptr(asmjit::x86::rsi));  // arg2

            // Преобразуем в целочисленные значения для побитового AND
            assembler.cvtsd2si(asmjit::x86::rax, asmjit::x86::xmm0);
            assembler.cvtsd2si(asmjit::x86::rbx, asmjit::x86::xmm1);

            // Выполняем побитовую операцию AND
            assembler.and_(asmjit::x86::rax, asmjit::x86::rbx);

            // Возвращаем результат в xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax);

            assembler.ret();
#endif
        }
    );
}
