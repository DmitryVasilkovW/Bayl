#include "../include/logicalOperations/NotEqualsGenerator.h"

jvalue NotEqualsGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Not Equals comparison requires exactly 2 arguments");
    }

    jboolean arg1 = getArgAs<jboolean>(args[0]);
    jboolean arg2 = getArgAs<jboolean>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateNotEqualsCode();
    });

    jvalue result;
    result.z = static_cast<jboolean>(cachedNotEqualsFunc(arg1, arg2));
    return result;
}

void NotEqualsGenerator::generateNotEqualsCode() {
    cachedNotEqualsFunc = [](jboolean a, jboolean b) -> bool {
        return a != b;
    };
}
