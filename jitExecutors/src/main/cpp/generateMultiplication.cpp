#include <asmjit/asmjit.h>

using namespace asmjit;

typedef int (*Func)(int, int);

void generateMultiplicationTemplate(CodeHolder& code) {
    JitRuntime runtime;

#ifdef __x86_64__
    x86::Assembler assembler(&code);
    assembler.mov(x86::eax, x86::edi);  // a -> eax
    assembler.mov(x86::ecx, x86::esi);  // b -> ecx
    assembler.mul(x86::ecx);            // eax = eax * ecx
    assembler.ret();
#elif defined(__arm64__)
    aarch64::Assembler assembler(&code);  // Исправлено с arm::Assembler на aarch64::Assembler
    assembler.mov(aarch64::x0, aarch64::x1);    // a -> x0
    assembler.mov(aarch64::x2, aarch64::x3);    // b -> x2
    assembler.mul(aarch64::x0, aarch64::x0, aarch64::x2); // x0 = x0 * x2
    assembler.ret();
#else
#error "Unsupported architecture"
#endif
}
