#pragma once

#include <asmjit/asmjit.h>
#include <mutex>
#include <memory>

class GlobalJitRuntime {
public:
    static GlobalJitRuntime& getInstance();
    asmjit::JitRuntime* getGlobalJitRuntime();

    GlobalJitRuntime(const GlobalJitRuntime&) = delete;
    GlobalJitRuntime& operator=(const GlobalJitRuntime&) = delete;

private:
    GlobalJitRuntime() = default;
};
