#include "include/JNIWrappers.h"
#include "include/JITGeneratorFactory.h"
#include "include/AbstractJITGenerator.h"

extern "C" {
    JNIEXPORT jint JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateMultiplicationTemplateInt(
        JNIEnv *env, jobject obj, jint arg1, jint arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("int_multiply");

            if (!generator) {
                return 0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.i;
        } catch (const std::exception& e) {
            return 0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateMultiplicationTemplateDouble(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("double_multiply");

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

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionRemainderTemplateDouble(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("double_div_remainder");

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

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionTemplateDouble(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("double_division");

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

    JNIEXPORT jint JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateAdditionTemplateInt(
        JNIEnv *env, jobject obj, jint arg1, jint arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("int_addition");

            if (!generator) {
                return 0;
            }

            std::vector<boost::any> args = {arg1, arg2};
            jvalue result = generator->generate(env, obj, args);

            return result.i;
        } catch (const std::exception& e) {
            return 0;
        }
    }

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateAdditionTemplateDouble(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    ) {
        try {
            auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("double_addition");

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
