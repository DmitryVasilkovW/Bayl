#pragma once
#include <boost/thread/once.hpp>
#include "../AbstractJITGenerator.h"
#include "../Utils.h"

class NotEqualsGenerator : public AbstractJITGenerator {
public:
    jvalue generate(
        JNIEnv* env,
        jobject obj,
        const std::vector<boost::any>& args
    ) override;

    std::unique_ptr<AbstractJITGenerator> clone() const override {
        return std::make_unique<NotEqualsGenerator>(*this);
    }

private:
    boost::once_flag initFlag;
    bool (*cachedNotEqualsFunc)(jboolean, jboolean) = nullptr;

    void generateNotEqualsCode();
};
