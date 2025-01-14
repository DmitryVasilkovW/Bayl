#pragma once
#include <boost/thread/once.hpp>
#include "../AbstractJITGenerator.h"

class IntAdditionGenerator : public AbstractJITGenerator {
public:
    jvalue generate(
        JNIEnv* env,
        jobject obj,
        const std::vector<boost::any>& args
    ) override;

   std::unique_ptr<AbstractJITGenerator> clone() const override {
        return std::make_unique<IntAdditionGenerator>(*this);
    }

private:
    boost::once_flag initFlag;
    jint (*cachedAddFunc)(jint, jint) = nullptr;

    void generateAdditionCode();
};
