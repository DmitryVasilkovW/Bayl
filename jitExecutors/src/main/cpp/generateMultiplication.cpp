#include <jni.h>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>

using namespace asmjit;

extern "C" JNIEXPORT jint JNICALL Java_org_bayl_JitExample_generateMultiplicationTemplate(JNIEnv *env, jobject obj, jint a, jint b) {
    CodeHolder code;
    code.init(Environment::host());
    JitRuntime runtime;

#ifdef __x86_64__
    x86::Assembler assembler(&code);
    assembler.mov(x86::eax, x86::edi);  // a -> eax
    assembler.mov(x86::ecx, x86::esi);  // b -> ecx
    assembler.mul(x86::ecx);            // eax = eax * ecx
    assembler.mov(x86::eax, result);    // Сохраняем результат в переменную
    assembler.ret();
#elif defined(__arm64__)
    a64::Assembler assembler(&code);

    a64::Gp aReg = a64::w0;  // Регистр для a
    a64::Gp bReg = a64::w1;  // Регистр для b
    a64::Gp resultReg = a64::w2;  // Регистр для результата

    assembler.mov(aReg, a);   // Загружаем a в регистр w0
    assembler.mov(bReg, b);   // Загружаем b в регистр w1

    assembler.mul(resultReg, aReg, bReg);  // w2 = w0 * w1
    assembler.mov(a64::w0, resultReg);
    assembler.ret(a64::x30);
#endif

    typedef jint (*MultiplyFunc)(jint, jint);
    MultiplyFunc func = nullptr;

    Error err = runtime.add(&func, &code);
    if (err) {
        return 0;
    }

    jint result = func(a, b);

    return result;
}
