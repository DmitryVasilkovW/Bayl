#include <jni.h>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>
#include <atomic>
#include <mutex>

#include "../GlobalJitRuntime.h"

using namespace asmjit;

static std::atomic<bool> isAdditionDoubleCodeGenerated{false};
static std::mutex generationAdditionDoubleMutex;
static jdouble (*cachedAddFunc)(jdouble, jdouble) = nullptr;

extern "C" JNIEXPORT jdouble JNICALL Java_org_bayl_JNIExample_generateAdditionTemplateDouble(JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2) {
    if (!isAdditionDoubleCodeGenerated.load(std::memory_order_acquire)) {
        std::lock_guard<std::mutex> lock(generationAdditionDoubleMutex);

        if (!isAdditionDoubleCodeGenerated.load(std::memory_order_relaxed)) {
            CodeHolder code;
            code.init(Environment::host());

#ifdef __arm64__
            a64::Assembler assembler(&code);

            a64::Vec aReg = a64::d0;   // Регистр для a
            a64::Vec bReg = a64::d1;   // Регистр для b
            a64::Vec resultReg = a64::d2;  // Регистр для результата

            assembler.fadd(resultReg, aReg, bReg);  // d2 = d0 + d1
            assembler.mov(a64::d0, resultReg);      // Перенос результата в d0
            assembler.ret(a64::x30);
#elif defined(__x86_64__)
            x86::Assembler assembler(&code);
            assembler.movsd(x86::xmm0, x86::xmm0);  // a -> xmm0
            assembler.addsd(x86::xmm0, x86::xmm1);  // xmm0 = xmm0 + xmm1
            assembler.ret();
#endif

            typedef jdouble (*AddFunc)(jdouble, jdouble);
            AddFunc func = nullptr;

            Error err = GlobalJitRuntime::getInstance().getGlobalJitRuntime()->add(&func, &code);
            if (err) {
                return 0;
            }

            cachedAddFunc = func;

            isAdditionDoubleCodeGenerated.store(true, std::memory_order_release);
        }
    }

    return cachedAddFunc(arg1, arg2);
}
