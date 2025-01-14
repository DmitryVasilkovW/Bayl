#include "include/JITGeneratorFactory.h"
#include "include/AbstractJITGenerator.h"
#include "include/arithmeticOperations/IntMultiplicationGenerator.h"
#include "include/arithmeticOperations/DoubleMultiplicationGenerator.h"
#include "include/arithmeticOperations/DoubleDivisionRemainderGenerator.h"
#include "include/arithmeticOperations/DoubleDivisionGenerator.h"
#include "include/arithmeticOperations/IntAdditionGenerator.h"
#include "include/arithmeticOperations/DoubleAdditionGenerator.h"

JITGeneratorFactory& JITGeneratorFactory::getInstance() {
    static JITGeneratorFactory instance;
    return instance;
}

void JITGeneratorFactory::registerGenerator(
    const std::string& type,
    std::function<std::unique_ptr<AbstractJITGenerator>()> creator
) {
    generators[type] = creator;
}

std::unique_ptr<AbstractJITGenerator> JITGeneratorFactory::createGenerator(
    const std::string& type
) {
    auto it = generators.find(type);
    if (it != generators.end()) {
        return it->second();
    }
    return nullptr;
}

static GeneratorRegistrar<IntMultiplicationGenerator>
    intMultiplyRegistrar("int_multiply");

static GeneratorRegistrar<DoubleMultiplicationGenerator>
    doubleMultiplyRegistrar("double_multiply");

static GeneratorRegistrar<DoubleDivisionRemainderGenerator>
    doubleDivRemRegistrar("double_div_remainder");

static GeneratorRegistrar<DoubleDivisionGenerator>
    doubleDivRegistrar("double_division");

static GeneratorRegistrar<IntAdditionGenerator>
    intAddRegistrar("int_addition");

static GeneratorRegistrar<DoubleAdditionGenerator>
    doubleAddRegistrar("double_addition");
