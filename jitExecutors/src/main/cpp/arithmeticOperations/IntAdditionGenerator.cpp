#include "../include/arithmeticOperations/IntAdditionGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue IntAdditionGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Addition requires exactly 2 arguments");
    }

    jint arg1 = getArgAs<jint>(args[0]);
    jint arg2 = getArgAs<jint>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateAdditionCode();
    });

    jvalue result;
    result.i = cachedAddFunc(arg1, arg2);
    return result;
}

void IntAdditionGenerator::generateAdditionCode() {
    asmjit::CodeHolder code;
    code.init(asmjit::Environment::host());

    cachedAddFunc = compileCode<jint(*)(jint, jint)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::w0;   // Регистр для a
            auto bReg = asmjit::a64::w1;   // Регистр для b
            auto resultReg = asmjit::a64::w2;  // Регистр для результата

            assembler.mov(aReg, aReg);
            assembler.mov(bReg, bReg);
            assembler.add(resultReg, aReg, bReg);  // w2 = w0 + w1
            assembler.mov(asmjit::a64::w0, resultReg);
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            assembler.mov(asmjit::x86::eax, asmjit::x86::edi);  // a -> eax
            assembler.mov(asmjit::x86::ecx, asmjit::x86::esi);  // b -> ecx
            assembler.add(asmjit::x86::eax, asmjit::x86::ecx);  // eax = eax + ecx
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
