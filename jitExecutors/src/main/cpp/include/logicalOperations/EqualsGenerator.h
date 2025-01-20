#pragma once
#include <boost/thread/once.hpp>
#include "../AbstractJITGenerator.h"
#include "../Utils.h"

class EqualsGenerator : public AbstractJITGenerator {
public:
    jvalue generate(
        JNIEnv* env,
        jobject obj,
        const std::vector<boost::any>& args
    ) override;

    std::unique_ptr<AbstractJITGenerator> clone() const override {
        return std::make_unique<EqualsGenerator>(*this);
    }

private:
    boost::once_flag initFlag;
    bool (*cachedEqualsFunc)(jboolean, jboolean) = nullptr;

    void generateEqualsCode();
};
