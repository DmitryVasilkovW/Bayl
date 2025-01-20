#include "../include/logicalOperations/GreaterGenerator.h"

jvalue GreaterGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Greater operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateGreaterCode();
    });

    jvalue result;
    result.z = cachedGreaterFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void GreaterGenerator::generateGreaterCode() {
    cachedGreaterFunc = [](jdouble a, jdouble b) -> bool {
        return a > b;
    };
}
