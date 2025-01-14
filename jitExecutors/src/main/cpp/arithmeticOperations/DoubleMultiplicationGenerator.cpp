#include "../include/arithmeticOperations/DoubleMultiplicationGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue DoubleMultiplicationGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Double multiplication requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateMultiplicationCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedMultiplyFuncDouble(arg1, arg2), 2);
    return result;
}

void DoubleMultiplicationGenerator::generateMultiplicationCode() {
    asmjit::CodeHolder code;
    code.init(asmjit::Environment::host());

    cachedMultiplyFuncDouble = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::d0;   // Регистр для a
            auto bReg = asmjit::a64::d1;   // Регистр для b
            auto resultReg = asmjit::a64::d2;  // Регистр для результата

            assembler.fmov(aReg, aReg);
            assembler.fmov(bReg, bReg);
            assembler.fmul(resultReg, aReg, bReg);  // d2 = d0 * d1
            assembler.fmov(asmjit::a64::d0, resultReg);
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            assembler.movsd(asmjit::x86::xmm0, asmjit::x86::xmm0);  // a -> xmm0
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::xmm1);  // b -> xmm1
            assembler.mulsd(asmjit::x86::xmm0, asmjit::x86::xmm1);  // xmm0 = xmm0 * xmm1
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
