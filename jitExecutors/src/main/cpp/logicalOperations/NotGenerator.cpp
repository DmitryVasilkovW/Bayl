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
    cachedNotFunc = [](jboolean a) -> bool {
        return !a;
    };
}

#undef ASMJIT_ASSEMBLER
