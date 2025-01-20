#include "../include/logicalOperations/NotEqualsDoubleGenerator.h"

jvalue NotEqualsDoubleGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 2) {
        throw std::runtime_error("NotEqualsDouble operation requires exactly 2 arguments");
    }

    jdouble arg1 = roundToPrecision(getArgAs<jdouble>(args[0]), 15);
    jdouble arg2 = roundToPrecision(getArgAs<jdouble>(args[1]), 15);

    boost::call_once(initFlag, [this]() {
        generateNotEqualsDoubleCode();
    });

    jvalue result;
    result.z = cachedNotEqualsDoubleFunc(arg1, arg2) ? JNI_TRUE : JNI_FALSE;
    return result;
}

void NotEqualsDoubleGenerator::generateNotEqualsDoubleCode() {
    cachedNotEqualsDoubleFunc = [](jdouble a, jdouble b) -> bool {
        return a != b;
    };
}
