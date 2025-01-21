#include "../include/logicalOperations/EqualsDoubleGenerator.h"

jvalue EqualsDoubleGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("EqualsDouble operation requires exactly 2 arguments");
    }

    jdouble arg1 = getArgAs<jdouble>(args[0]);
    jdouble arg2 = getArgAs<jdouble>(args[1]);

    boost::call_once(initFlag, [this]() {
        generateEqualsDoubleCode();
    });

    jvalue result;
    result.z = cachedEqualsDoubleFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void EqualsDoubleGenerator::generateEqualsDoubleCode() {
    cachedEqualsDoubleFunc = [](jdouble a, jdouble b) -> bool {
        return a == b;
    };
}
