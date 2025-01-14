#include "../include/arithmeticOperations/DoubleSubtractionGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue DoubleSubtractionGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Subtraction requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateSubtractionCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedSubFunc(arg1, arg2), 15);
    return result;
}

void DoubleSubtractionGenerator::generateSubtractionCode() {
    cachedSubFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::d0;   // Регистр для a
            auto bReg = asmjit::a64::d1;   // Регистр для b
            auto resultReg = asmjit::a64::d2;  // Регистр для результата

            assembler.fsub(resultReg, aReg, bReg);  // d2 = d0 - d1
            assembler.mov(asmjit::a64::d0, resultReg);  // Перенос результата в d0
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            assembler.subsd(asmjit::x86::xmm0, asmjit::x86::xmm1);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
