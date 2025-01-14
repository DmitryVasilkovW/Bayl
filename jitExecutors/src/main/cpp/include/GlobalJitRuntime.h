#pragma once

#include <boost/noncopyable.hpp>
#include <boost/thread/once.hpp>
#include <boost/smart_ptr/shared_ptr.hpp>
#include <boost/smart_ptr/make_shared.hpp>
#include <asmjit/asmjit.h>

class GlobalJitRuntime : private boost::noncopyable {
public:
    static GlobalJitRuntime& getInstance();
    asmjit::JitRuntime* getGlobalJitRuntime();

private:
    GlobalJitRuntime() = default;

    boost::once_flag initFlag;
    boost::shared_ptr<asmjit::JitRuntime> globalRuntime;
};
