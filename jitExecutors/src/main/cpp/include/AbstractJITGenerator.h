#pragma once
#include <jni.h>
#include <boost/any.hpp>
#include <functional>
#include <vector>
#include <memory>
#include <stdexcept>
#include <asmjit/asmjit.h>
#include <asmjit/a64.h>

#include "GlobalJitRuntime.h"
#include "JITGeneratorFactory.h"
#include "AbstractJITGenerator.h"

#ifdef __arm64__
    #define ASMJIT_ASSEMBLER asmjit::a64::Assembler
#elif defined(__x86_64__)
    #define ASMJIT_ASSEMBLER asmjit::x86::Assembler
#else
    #error "Unsupported architecture"
#endif

class AbstractJITGenerator {
public:
    virtual ~AbstractJITGenerator() = default;

    virtual jvalue generate(
        JNIEnv* env,
        jobject obj,
        const std::vector<boost::any>& args
    ) = 0;

    virtual std::unique_ptr<AbstractJITGenerator> clone() const = 0;

protected:
    template<typename T>
    T getArgAs(const boost::any& arg) {
        try {
            return boost::any_cast<T>(arg);
        } catch (const boost::bad_any_cast& e) {
            throw std::runtime_error("Invalid argument type: " + std::string(e.what()));
        }
    }

    template<typename FuncType>
    FuncType compileCode(
        std::function<void(ASMJIT_ASSEMBLER&)> assemblerCallback
    ) {
        asmjit::CodeHolder code;
        code.init(asmjit::Environment::host());

        FuncType func = nullptr;
        asmjit::Error err;

        ASMJIT_ASSEMBLER assembler(&code);
        assemblerCallback(assembler);

        GlobalJitRuntime& runtime = GlobalJitRuntime::getInstance();
        err = runtime.getGlobalJitRuntime()->add(&func, &code);

        if (err) {
            throw std::runtime_error("JIT Code generation failed");
        }

        return func;
    }

private:
    std::shared_ptr<GlobalJitRuntime> jitRuntime;
};
