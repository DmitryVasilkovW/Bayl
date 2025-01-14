#include <jni.h>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>
#include <atomic>
#include <mutex>
#include <cmath>

#include "../GlobalJitRuntime.h"
#include "../Utils.h"

using namespace asmjit;

static std::atomic<bool> isDivisionRemainderDoubleCodeGenerated{false};
static std::mutex generationDivisionRemainderDoubleMutex;
static jdouble (*cachedDivRemFunc)(jdouble, jdouble) = nullptr;

extern "C" JNIEXPORT jdouble JNICALL Java_org_bayl_JNIExample_generateDivisionRemainderTemplateDouble(JNIEnv *env, jobject obj, jdouble arg1, jdouble arg2) {
    if (!isDivisionRemainderDoubleCodeGenerated.load(std::memory_order_acquire)) {
        std::lock_guard<std::mutex> lock(generationDivisionRemainderDoubleMutex);

        if (!isDivisionRemainderDoubleCodeGenerated.load(std::memory_order_relaxed)) {
            CodeHolder code;
            code.init(Environment::host());

#ifdef __arm64__
            a64::Assembler assembler(&code);

            // Загружаем значения в регистры
            assembler.fmov(a64::d0, a64::d0); // Делимое
            assembler.fmov(a64::d1, a64::d1); // Делитель

            // Вычисление частного: d2 = floor(d0 / d1)
            assembler.fdiv(a64::d2, a64::d0, a64::d1);
            assembler.frintz(a64::d2, a64::d2); // Округление вниз до целого

            // Вычисление остатка: d0 = d0 - (d2 * d1)
            assembler.fmul(a64::d3, a64::d2, a64::d1); // d3 = d2 * d1
            assembler.fsub(a64::d0, a64::d0, a64::d3); // d0 = d0 - d3 (остаток)

            assembler.ret(a64::x30);
#elif defined(__x86_64__)
            x86::Assembler assembler(&code);

            // Загружаем значения в регистры
            assembler.movsd(x86::xmm0, x86::xmm0); // arg1 -> xmm0 (dividend)
            assembler.movsd(x86::xmm1, x86::xmm1); // arg2 -> xmm1 (divisor)

            // Копируем делимое в xmm2
            assembler.movapd(x86::xmm2, x86::xmm0);

            // Вычисление частного: xmm2 = xmm0 / xmm1
            assembler.divsd(x86::xmm2, x86::xmm1);

            // Обрезаем дробную часть частного: xmm2 = floor(xmm2)
            assembler.roundsd(x86::xmm2, x86::xmm2, 1); // 1 = округление вниз (floor)

            // Вычисление остатка: xmm0 = xmm0 - (xmm2 * xmm1)
            assembler.mulsd(x86::xmm2, x86::xmm1); // xmm2 = quotient * divisor
            assembler.subsd(x86::xmm0, x86::xmm2); // xmm0 = dividend - xmm2 (remainder)

            assembler.ret();
#endif

            typedef jdouble (*DivRemFunc)(jdouble, jdouble);
            DivRemFunc func = nullptr;

            Error err = GlobalJitRuntime::getInstance().getGlobalJitRuntime()->add(&func, &code);
            if (err) {
                return 0;
            }

            cachedDivRemFunc = func;

            isDivisionRemainderDoubleCodeGenerated.store(true, std::memory_order_release);
        }
    }

    return roundToPrecision(cachedDivRemFunc(arg1, arg2), 15);
}
