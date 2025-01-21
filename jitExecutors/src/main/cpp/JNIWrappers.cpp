#include "include/JNIWrappers.h"
#include "include/JITGeneratorFactory.h"
#include "include/AbstractJITGenerator.h"

extern "C" {
JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateMultiplicationTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("multiply");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDivisionRemainderTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("div_remainder");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDivisionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("division");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateAdditionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("addition");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateSubtractionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("subtraction");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateIncrementTemplate(
        JNIEnv *env, jobject obj, jdouble arg1
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("increment");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDecrementTemplate(
        JNIEnv *env, jobject obj, jdouble arg1
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("decrement");

        if (!generator) {
            return 0.0;
        }

        std::vector <boost::any> args = {arg1};
        jvalue result = generator->generate(env, obj, args);

        return result.d;
    } catch (const std::exception &e) {
        return 0.0;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateAndTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("and");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotTemplate(
        JNIEnv *env, jobject obj, jboolean arg1
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("not");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateOrTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("or");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateGreaterEqualTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("greater_equal");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateLessEqualTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("less_equal");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateEqualsTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("equals");

        if (!generator) {
            return false;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z;
    } catch (const std::exception &e) {
        return false;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotEqualsTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("notequals");

        if (!generator) {
            return false;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z;
    } catch (const std::exception &e) {
        return false;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateGreaterTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("greater");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateLessTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("less");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateEqualsDoubleTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("equals_double");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotEqualsDoubleTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
) {
    try {
        auto generator = JITGeneratorFactory::getInstance()
                .createGenerator("not_equals_double");

        if (!generator) {
            return JNI_FALSE;
        }

        std::vector <boost::any> args = {arg1, arg2};
        jvalue result = generator->generate(env, obj, args);

        return result.z ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception &e) {
        return JNI_FALSE;
    }
}
}
