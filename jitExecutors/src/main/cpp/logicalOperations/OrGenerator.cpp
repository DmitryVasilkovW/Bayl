#include "../include/logicalOperations/OrGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue OrGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("OR operation requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateOrCode();
    });

    jvalue result;
    result.z = cachedOrFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void OrGenerator::generateOrCode() {
    cachedOrFunc = [](jboolean a, jboolean b) -> bool {
        return a || b;
    };
}

#undef ASMJIT_ASSEMBLER
