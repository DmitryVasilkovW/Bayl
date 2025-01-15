#include "../include/arithmeticOperations/AdditionGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue AdditionGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Addition requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateAdditionCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedAddFunc(arg1, arg2), 15);
    return result;
}

void AdditionGenerator::generateAdditionCode() {
    cachedAddFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::d0;   // Регистр для a
            auto bReg = asmjit::a64::d1;   // Регистр для b
            auto resultReg = asmjit::a64::d2;  // Регистр для результата

            assembler.fadd(resultReg, aReg, bReg);  // d2 = d0 + d1
            assembler.mov(asmjit::a64::d0, resultReg);  // Перенос результата в d0
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            assembler.addsd(asmjit::x86::xmm0, asmjit::x86::xmm1);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
