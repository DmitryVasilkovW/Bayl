#include "GlobalJitRuntime.h"

GlobalJitRuntime& GlobalJitRuntime::getInstance() {
    static GlobalJitRuntime instance;
    return instance;
}

asmjit::JitRuntime* GlobalJitRuntime::getGlobalJitRuntime() {
    static boost::once_flag initFlag;
    static boost::shared_ptr<asmjit::JitRuntime> runtime;

    boost::call_once(initFlag, []() {
        runtime = boost::make_shared<asmjit::JitRuntime>();
    });

    return runtime.get();
}
