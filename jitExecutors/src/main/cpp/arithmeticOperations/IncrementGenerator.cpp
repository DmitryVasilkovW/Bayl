#include "../include/arithmeticOperations/IncrementGenerator.h"
#include "../include/GlobalVariables.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__) || defined(__i386__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

double __minus_one_double = -1.0;

jvalue IncrementGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 1) {
        throw std::runtime_error("Increment requires exactly 1 argument");
    }

    jdouble arg = getArgAs<jdouble>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateIncrementCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedIncrementFunc(arg), 15);
    return result;
}

void IncrementGenerator::generateIncrementCode() {
    cachedIncrementFunc = compileCode<jdouble(*)(jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto argReg = asmjit::a64::d0;   // Регистр для аргумента

            // Загружаем константу 1.0 в отдельный регистр
            assembler.fmov(asmjit::a64::d1, 1.0);

            // Выполняем сложение с 1.0
            assembler.fadd(argReg, argReg, asmjit::a64::d1);

            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__) || defined(__i386__)
            // Загружаем константу 1.0 в регистр xmm1
            assembler.mov(asmjit::x86::rax, reinterpret_cast<uintptr_t>(&__minus_one_double)); // Загружаем адрес в rax
            assembler.movsd(asmjit::x86::xmm1, asmjit::x86::ptr(asmjit::x86::rax)); // Загружаем значение по адресу в xmm1

            // Выполняем вычитание 1.0 из аргумента в xmm0
            assembler.subsd(asmjit::x86::xmm0, asmjit::x86::xmm1);

            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
