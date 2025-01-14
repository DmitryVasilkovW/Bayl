#pragma once
#include <jni.h>

extern "C" {
    JNIEXPORT jint JNICALL Java_org_bayl_JNIExample_generateMultiplicationTemplateInt(
        JNIEnv *env, jobject obj, jint arg1, jint arg2
    );

    JNIEXPORT jdouble JNICALL Java_org_bayl_JNIExample_generateMultiplicationTemplateDouble(
        JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2
    );
}
