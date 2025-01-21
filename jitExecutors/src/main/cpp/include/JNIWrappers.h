#pragma once

#include <jni.h>

extern "C" {
JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateMultiplicationTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDivisionRemainderTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDivisionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateAdditionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateSubtractionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateIncrementTemplate(
        JNIEnv *env, jobject obj, jdouble arg1
);

JNIEXPORT jdouble
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateDecrementTemplate(
        JNIEnv *env, jobject obj, jdouble arg1
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateAndTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotTemplate(
        JNIEnv *env, jobject obj, jboolean arg1
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateOrTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateGreaterEqualTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateLessEqualTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateEqualsTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotEqualsTemplate(
        JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateGreaterTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateLessTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateEqualsDoubleTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);

JNIEXPORT jboolean
JNICALL Java_org_bayl_runtime_compile_jit_JITExecutorsWrapper_generateNotEqualsDoubleTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
);
}
