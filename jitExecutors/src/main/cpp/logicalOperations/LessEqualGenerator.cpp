#include "../include/logicalOperations/LessEqualGenerator.h"

jvalue LessEqualGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("LessEqual operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateLessEqualCode();
    });

    jvalue result;
    result.z = cachedLessEqualFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void LessEqualGenerator::generateLessEqualCode() {
    cachedLessEqualFunc = [](jdouble a, jdouble b) -> bool {
        return a <= b;
    };
}
