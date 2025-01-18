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

    jboolean arg1 = getArgAs<jboolean>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateNotCode();
    });

    jvalue result;
    result.z = cachedNotFunc(arg1); // Using jboolean as the result
    return result;
}

void NotGenerator::generateNotCode() {
    cachedNotFunc = compileCode<jboolean(*)(jboolean)>(  // Using jboolean here
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto aReg = asmjit::a64::x0;   // Register for the argument
            auto resultReg = asmjit::a64::x1;  // Register for the result

            // Logical NOT operation: XOR with 1 to invert (0 -> 1, 1 -> 0)
            assembler.eor(resultReg, aReg, asmjit::a64::xzr); // x1 = x0 ^ 1

            // Move the result back to the return register (x0)
            assembler.mov(asmjit::a64::x0, resultReg);
            assembler.ret();
#elif defined(__x86_64__)
            // Logical NOT operation: Test and invert (0 -> 1, 1 -> 0)
            assembler.test(asmjit::x86::rdi, asmjit::x86::rdi);  // Test if rdi is non-zero
            assembler.setnz(asmjit::x86::al);  // Set AL to 1 if non-zero, 0 if zero

            // Invert the value
            assembler.xor_(asmjit::x86::al, 1);  // XOR AL with 1 to invert

            // Move the result back to rdi (for returning)
            assembler.movzx(asmjit::x86::rdi, asmjit::x86::al);  // Move AL (0 or 1) to rdi
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
