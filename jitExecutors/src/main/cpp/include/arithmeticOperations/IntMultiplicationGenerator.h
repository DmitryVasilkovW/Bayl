#pragma once
#include "../AbstractJITGenerator.h"
#include <boost/thread/once.hpp>

class IntMultiplicationGenerator : public AbstractJITGenerator {
public:
    IntMultiplicationGenerator() = default;

    jvalue generate(
        JNIEnv* env,
        jobject obj,
        const std::vector<boost::any>& args
    ) override;

    std::unique_ptr<AbstractJITGenerator> clone() const override {
        return std::make_unique<IntMultiplicationGenerator>(*this);
    }

private:
    boost::once_flag initFlag;
    jint (*cachedMultiplyFunc)(jint, jint) = nullptr;
    void generateMultiplicationCode();
};
