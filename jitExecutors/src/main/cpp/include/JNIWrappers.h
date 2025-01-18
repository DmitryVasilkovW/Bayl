#pragma once
#include <jni.h>

extern "C" {
    JNIEXPORT jdouble JNICALL Java_org_bayl_JNIExample_generateMultiplicationTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionRemainderTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDivisionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateAdditionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateSubtractionTemplate(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateIncrementTemplate(
        JNIEnv *env, jobject obj, jdouble arg1
    );

     JNIEXPORT jdouble JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateDecrementTemplate(
         JNIEnv *env, jobject obj, jdouble arg1
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateAndTemplate(
         JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateNotTemplate(
        JNIEnv *env, jobject obj, jboolean arg1
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateOrTemplate(
         JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateGreaterEqualTemplate(
         JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateLessEqualTemplate(
         JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateEqualsTemplate(
         JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
     );

     JNIEXPORT jboolean JNICALL Java_org_bayl_vm_executor_JITExecutorsWrapper_generateNotEqualsTemplate(
         JNIEnv *env, jobject obj, jboolean arg1, jboolean arg2
     );
}
