#include "../include/arithmeticOperations/IntMultiplicationGenerator.h"
#include <asmjit/asmjit.h>

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue IntMultiplicationGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Requires exactly 2 arguments");
    }

    jint arg1 = getArgAs<jint>(args[0]);
    jint arg2 = getArgAs<jint>(args[1]);

    boost::call_once(initFlag, [this]() {
        cachedMultiplyFunc = compileCode<jint(*)(jint, jint)>(
            [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
        asmjit::a64::Gp aReg = asmjit::a64::w0;   // Регистр для a
        asmjit::a64::Gp bReg = asmjit::a64::w1;   // Регистр для b
        asmjit::a64::Gp resultReg = asmjit::a64::w2;  // Регистр для результата

        assembler.mov(aReg, aReg);
        assembler.mov(bReg, bReg);
        assembler.mul(resultReg, aReg, bReg);  // w2 = w0 * w1
        assembler.mov(asmjit::a64::w0, resultReg);
        assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
        asmjit::x86::Assembler assembler(&code);
        assembler.mov(asmjit::x86::eax, asmjit::x86::edi);  // a -> eax
        assembler.mov(asmjit::x86::ecx, asmjit::x86::esi);  // b -> ecx
        assembler.mul(asmjit::x86::ecx);            // eax = eax * ecx
        assembler.ret();
#endif
            }
        );
    });

    jvalue result;
    result.i = cachedMultiplyFunc(arg1, arg2);
    return result;
}

#undef ASMJIT_ASSEMBLER
