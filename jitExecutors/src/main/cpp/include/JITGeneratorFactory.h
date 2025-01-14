#pragma once
#include <memory>
#include <string>
#include <unordered_map>
#include <functional>

class AbstractJITGenerator;

class JITGeneratorFactory {
public:
    static JITGeneratorFactory& getInstance();

    void registerGenerator(
        const std::string& type,
        std::function<std::unique_ptr<AbstractJITGenerator>()> creator
    );

    std::unique_ptr<AbstractJITGenerator> createGenerator(
        const std::string& type
    );

private:
    JITGeneratorFactory() = default;

    std::unordered_map<
        std::string,
        std::function<std::unique_ptr<AbstractJITGenerator>()>
    > generators;
};

template<typename GeneratorType>
class GeneratorRegistrar {
public:
    GeneratorRegistrar(const std::string& type) {
        JITGeneratorFactory::getInstance().registerGenerator(
            type,
            []() { return std::make_unique<GeneratorType>(); }
        );
    }
};
