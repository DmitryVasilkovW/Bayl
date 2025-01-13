#include "GlobalJitRuntime.h"

GlobalJitRuntime& GlobalJitRuntime::getInstance() {
    static GlobalJitRuntime instance;
    return instance;
}

asmjit::JitRuntime* GlobalJitRuntime::getGlobalJitRuntime() {
    static std::mutex runtimeMutex;
    std::lock_guard<std::mutex> lock(runtimeMutex);

    static std::unique_ptr<asmjit::JitRuntime> globalRuntime;
    if (!globalRuntime) {
        globalRuntime = std::make_unique<asmjit::JitRuntime>();
    }

    return globalRuntime.get();
}
