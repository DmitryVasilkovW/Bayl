#include "../include/logicalOperations/NotGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

jvalue NotGenerator::generate(
    JNIEnv* env,
    jobject obj,
    const std::vector<boost::any>& args
) {
    if (args.size() != 1) {
        throw std::runtime_error("Not operation requires exactly 1 argument");
    }

    jdouble arg = getArgAs<jdouble>(args[0]);

    boost::call_once(initFlag, [this]() {
        generateNotCode();
    });

    jvalue result;
    result.d = roundToPrecision(cachedNotFunc(arg), 15);
    return result;
}

void NotGenerator::generateNotCode() {
    cachedNotFunc = compileCode<jdouble(*)(jdouble)>(
        [](ASMJIT_ASSEMBLER& assembler) {
#ifdef __arm64__
            auto argReg = asmjit::a64::d0;   // Регистр для аргумента
            auto resultReg = asmjit::a64::d1;  // Регистр для результата

            // Преобразуем аргумент в целое число (побитовое отрицание работает с целыми числами)
            assembler.fcvtzs(asmjit::a64::x2, argReg);  // Преобразование в целое (int)

            // Применяем побитовое отрицание
            assembler.not_(asmjit::a64::x2, asmjit::a64::x2); // Побитовая инверсия
            assembler.mov(asmjit::a64::d0, asmjit::a64::x2); // Перенос результата обратно в d0
            assembler.ret(asmjit::a64::x30);
#elif defined(__x86_64__)
            // Преобразуем аргумент в целое число
            assembler.cvtsd2si(asmjit::x86::rax, asmjit::x86::xmm0); // Преобразование double в int64_t

            // Применяем побитовое отрицание
            assembler.not_(asmjit::x86::rax);  // Побитовая инверсия

            // Переводим обратно в xmm0
            assembler.cvtsi2sd(asmjit::x86::xmm0, asmjit::x86::rax);
            assembler.ret();
#endif
        }
    );
}

#undef ASMJIT_ASSEMBLER
