#include "../include/logicalOperations/NotGenerator.h"

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
    result.z = cachedNotFunc(arg1);
    return result;
}

void NotGenerator::generateNotCode() {
    cachedNotFunc = [](jboolean a) -> bool {
        return !a;
    };
}
