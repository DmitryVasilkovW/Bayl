#include "../include/arithmeticOperations/DoubleDivisionGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue DoubleDivisionGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Division requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateDivisionCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedDivFunc(arg1, arg2), 15);
    return result;
}

void DoubleDivisionGenerator::generateDivisionCode() {
    asmjit::CodeHolder code;
    code.init(asmjit::Environment::host());

    cachedDivFunc = compileCode<jdouble(*)(jdouble, jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto dividend = asmjit::a64::d0;
            auto divisor = asmjit::a64::d1;

            assembler.fdiv(dividend, dividend, divisor);
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            assembler.divsd(asmjit::x86::xmm0, asmjit::x86::xmm1);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
