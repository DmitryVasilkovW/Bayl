#include "../include/logicalOperations/AndGenerator.h"

jvalue AndGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("AND operation requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateAndCode();
    });

    jvalue result;
    result.z = cachedAndFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;  // Правильная логика для AND
    return result;
}

void AndGenerator::generateAndCode() {
    cachedAndFunc = [](jboolean a, jboolean b) -> bool {
        return a && b;
    };
}
