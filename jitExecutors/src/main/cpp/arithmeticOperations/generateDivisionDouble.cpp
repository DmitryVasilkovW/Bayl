#include <jni.h>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>
#include <atomic>
#include <mutex>

#include "../GlobalJitRuntime.h"
#include "../Utils.h"

using namespace asmjit;

static std::atomic<bool> isDivisionDoubleCodeGenerated{false};
static std::mutex generationDivisionDoubleMutex;
static jdouble (*cachedDivFunc)(jdouble, jdouble) = nullptr;

extern "C" JNIEXPORT jdouble JNICALL Java_org_bayl_JNIExample_generateDivisionTemplateDouble(JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2) {
    if (!isDivisionDoubleCodeGenerated.load(std::memory_order_acquire)) {
        std::lock_guard<std::mutex> lock(generationDivisionDoubleMutex);

        if (!isDivisionDoubleCodeGenerated.load(std::memory_order_relaxed)) {
            CodeHolder code;
            code.init(Environment::host());

#ifdef __arm64__
            a64::Assembler assembler(&code);

            // Простое деление: результат в d0
            assembler.fdiv(a64::d0, a64::d0, a64::d1);
            assembler.ret(a64::x30);

#elif defined(__x86_64__)
            x86::Assembler assembler(&code);

            // Простое деление: результат в xmm0
            assembler.divsd(x86::xmm0, x86::xmm1);
            assembler.ret();
#endif

            typedef jdouble (*DivFunc)(jdouble, jdouble);
            DivFunc func = nullptr;

            Error err = GlobalJitRuntime::getInstance().getGlobalJitRuntime()->add(&func, &code);
            if (err) {
                return 0;
            }

            cachedDivFunc = func;

            isDivisionDoubleCodeGenerated.store(true, std::memory_order_release);
        }
    }

    return roundToPrecision(cachedDivFunc(arg1, arg2), 15);
}
