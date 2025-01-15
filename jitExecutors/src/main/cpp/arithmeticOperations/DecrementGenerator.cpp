#include "../include/arithmeticOperations/DecrementGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue DecrementGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 1) {
        throw std::runtime_error("Decrement requires exactly 1 argument");
    }

    jdouble arg = getArgAs<jdouble>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateDecrementCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedDecrementFunc(arg), 15);
    return result;
}

void DecrementGenerator::generateDecrementCode() {
    cachedDecrementFunc = compileCode<jdouble(*)(jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto argReg = asmjit::a64::d0;   // Регистр для аргумента

            // Загружаем константу 1.0 в отдельный регистр
            assembler.fmov(asmjit::a64::d1, 1.0);

            // Выполняем вычитание 1.0
            assembler.fsub(argReg, argReg, asmjit::a64::d1);

            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            // Загружаем константу 1.0
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::ptr_abs("__one_double"));

            // Выполняем вычитание 1.0
            assembler.subsd(asmjit::x86::xmm0, asmjit::x86::xmm1);

            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
