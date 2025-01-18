#include "../include/logicalOperations/EqualsGenerator.h"

jvalue EqualsGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Equals comparison requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateEqualsCode();
    });

    jvalue result;
    result.z = static_cast<jboolean>(cachedEqualsFunc(arg1, arg2));
    return result;
}

void EqualsGenerator::generateEqualsCode() {
    cachedEqualsFunc = [](jboolean a, jboolean b) -> bool {
        return a == b;
    };
}
