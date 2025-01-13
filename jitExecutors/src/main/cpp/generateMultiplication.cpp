#include <jni.h>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>
#include <atomic>
#include <mutex>

#include "GlobalJitRuntime.h"

using namespace asmjit;

static std::atomic<bool> isMultiplicationIntCodeGenerated{false};
static std::mutex generationMultiplicationIntMutex;
static jint (*cachedMultiplyFunc)(jint, jint) = nullptr;

extern "C" JNIEXPORT jint JNICALL Java_org_bayl_JNIExample_generateMultiplicationTemplateInt(JNIEnv *env, jobject obj, jint a, jint b) {
    if (!isMultiplicationIntCodeGenerated.load(std::memory_order_acquire)) {
        std::lock_guard<std::mutex> lock(generationMultiplicationIntMutex);

        if (!isMultiplicationIntCodeGenerated.load(std::memory_order_relaxed)) {
            CodeHolder code;
            code.init(Environment::host());

#ifdef __arm64__
            a64::Assembler assembler(&code);

            a64::Gp aReg = a64::w0;   // Регистр для a
            a64::Gp bReg = a64::w1;   // Регистр для b
            a64::Gp resultReg = a64::w2;  // Регистр для результата

            assembler.mov(aReg, aReg);
            assembler.mov(bReg, bReg);
            assembler.mul(resultReg, aReg, bReg);  // w2 = w0 * w1
            assembler.mov(a64::w0, resultReg);
            assembler.ret(a64::x30);
#elif defined(__x86_64__)
            x86::Assembler assembler(&code);
            assembler.mov(x86::eax, x86::edi);  // a -> eax
            assembler.mov(x86::ecx, x86::esi);  // b -> ecx
            assembler.mul(x86::ecx);            // eax = eax * ecx
            assembler.mov(x86::eax, result);    // Сохраняем результат в переменную
            assembler.ret();
#endif

            typedef jint (*MultiplyFunc)(jint, jint);
            MultiplyFunc func = nullptr;

            Error err = GlobalJitRuntime::getInstance().getGlobalJitRuntime()->add(&func, &code);
            if (err) {
                return 0;
            }

            cachedMultiplyFunc = func;

            isMultiplicationIntCodeGenerated.store(true, std::memory_order_release);
        }
    }

    return cachedMultiplyFunc(a, b);
}
