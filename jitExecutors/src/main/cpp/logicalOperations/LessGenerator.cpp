#include "../include/logicalOperations/LessGenerator.h"

jvalue LessGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("Less operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateLessCode();
    });

    jvalue result;
    result.z = cachedLessFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void LessGenerator::generateLessCode() {
    cachedLessFunc = [](jdouble a, jdouble b) -> bool {
        return a < b;
    };
}
