#include "include/JNIWrappers.h"
#include "include/JITGeneratorFactory.h"
#include "include/AbstractJITGenerator.h"

extern "C" {
    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateMultiplicationTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("multiply");

            if (!generator) {
                return 0.0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.d;
        } catch (const std::exception& e) {
            return 0.0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionRemainderTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("div_remainder");

            if (!generator) {
                return 0.0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.d;
        } catch (const std::exception& e) {
            return 0.0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("division");

            if (!generator) {
                return 0.0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.d;
        } catch (const std::exception& e) {
            return 0.0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateAdditionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("addition");

            if (!generator) {
                return 0.0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.d;
        } catch (const std::exception& e) {
            return 0.0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateSubtractionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("subtraction");

            if (!generator) {
                return 0.0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.d;
        } catch (const std::exception& e) {
            return 0.0;
        }
    }
}
