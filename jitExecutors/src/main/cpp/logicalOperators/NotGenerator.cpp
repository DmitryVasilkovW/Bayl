#include "../include/logicalOperations/NotGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue NotGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 1) {
        throw std::runtime_error("NOT operation requires exactly 1 argument");
    }

    bool arg1 = getArgAs<bool>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateNotCode();
    });

    jvalue result;
    result.z = cachedNotFunc(arg1); // Using bool as the result
    return result;
}

void NotGenerator::generateNotCode() {
    cachedNotFunc = compileCode<jboolean(*)(jboolean)>(  // Ensure we're using jboolean here
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::x0;   // Register for the argument
            auto resultReg = asmjit::a64::x1;  // Register for the result

            // Logical NOT operation
            assembler.eor(resultReg, aReg, asmjit::a64::xzr); // x1 = x0 ^ 1

            // Move the result back to the return register
            assembler.mov(asmjit::a64::x0, resultReg);
            assembler.ret();
#elif defined(__x86_64__)
            // Logical NOT operation (using assembler.test)
            assembler.test(asmjit::x86::rdi, asmjit::x86::rdi);  // Test if non-zero
            assembler.setnz(asmjit::x86::al);  // Set 1 if non-zero, 0 if zero

            // Move the result back to xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::al);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
