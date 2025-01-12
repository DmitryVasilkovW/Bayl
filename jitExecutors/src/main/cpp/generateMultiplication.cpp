#include <asmjit/asmjit.h>
#include <asmjit/a64.h>

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
    a64::Assembler assembler(&code);
    assembler.mov(a64::w2, a64::w0);   // a -> w2
    assembler.mul(a64::w0, a64::w2, a64::w1); // w0 = w2 * w1 (a * b)
#else
#error "Unsupported architecture"
#endif
}
