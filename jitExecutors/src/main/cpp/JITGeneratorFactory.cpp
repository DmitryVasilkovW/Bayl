#include "include/JITGeneratorFactory.h"
#include "include/AbstractJITGenerator.h"
#include "include/arithmeticOperations/MultiplicationGenerator.h"
#include "include/arithmeticOperations/DivisionRemainderGenerator.h"
#include "include/arithmeticOperations/DivisionGenerator.h"
#include "include/arithmeticOperations/AdditionGenerator.h"
#include "include/arithmeticOperations/SubtractionGenerator.h"
#include "include/arithmeticOperations/IncrementGenerator.h"
#include "include/arithmeticOperations/DecrementGenerator.h"
#include "include/logicalOperations/AndGenerator.h"
#include "include/logicalOperations/NotGenerator.h"
#include "include/logicalOperations/OrGenerator.h"
#include "include/logicalOperations/GreaterEqualGenerator.h"
#include "include/logicalOperations/LessEqualGenerator.h"
#include "include/logicalOperations/EqualsGenerator.h"
#include "include/logicalOperations/NotEqualsGenerator.h"

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

static GeneratorRegistrar<MultiplicationGenerator>
    multiplyRegistrar("multiply");

static GeneratorRegistrar<DivisionRemainderGenerator>
    divRemRegistrar("div_remainder");

static GeneratorRegistrar<DivisionGenerator>
    divRegistrar("division");

static GeneratorRegistrar<AdditionGenerator>
    addRegistrar("addition");

static GeneratorRegistrar<SubtractionGenerator>
    subRegistrar("subtraction");

static GeneratorRegistrar<IncrementGenerator>
    incRegistrar("increment");

static GeneratorRegistrar<DecrementGenerator>
    decRegistrar("decrement");

static GeneratorRegistrar<AndGenerator>
    andRegistrar("and");

static GeneratorRegistrar<NotGenerator>
    notRegistrar("not");

static GeneratorRegistrar<OrGenerator>
    orRegistrar("or");

static GeneratorRegistrar<GreaterEqualsGenerator>
    geRegistrar("greater_equals");

static GeneratorRegistrar<LessEqualGenerator>
    leRegistrar("less_equal");

static GeneratorRegistrar<EqualsGenerator>
    eqlsRegistrar("equals");

static GeneratorRegistrar<NotEqualsGenerator>
    noteqlsRegistrar("notequals");
