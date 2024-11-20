#include <iostream>
#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/Module.h>
#include <llvm/IR/Verifier.h>
#include <llvm/ExecutionEngine/ExecutionEngine.h>
#include <llvm/ExecutionEngine/MCJIT.h>
#include <llvm/ExecutionEngine/GenericValue.h>
#include <llvm/Support/TargetSelect.h>

using namespace llvm;

int main() {
	InitializeNativeTarget();
	InitializeNativeTargetAsmPrinter();
	LLVMContext context;

	auto module = std::make_unique<Module>("MySimpleLang", context);
	IRBuilder<> builder(context);

	FunctionType *funcType = FunctionType::get(Type::getInt32Ty(context), false);
	Function *mainFunc = Function::Create(funcType, Function::ExternalLinkage, "main", module.get());

	BasicBlock *entry = BasicBlock::Create(context, "entry", mainFunc);
	builder.SetInsertPoint(entry);

	Value *lhs = builder.getInt32(4);
	Value *rhs = builder.getInt32(228);
	Value *sum = builder.CreateAdd(lhs, rhs, "sum");

	builder.CreateRet(sum);

	if (verifyFunction(*mainFunc)) {
		std::cerr << "Error constructing function!\n";
		return 1;
	}

	module->print(llvm::errs(), nullptr);

	std::string errStr;
	ExecutionEngine *execEngine = EngineBuilder(std::move(module))
			.setErrorStr(&errStr)
			.setEngineKind(EngineKind::JIT)
			.create();

	if (!execEngine) {
		std::cerr << "Failed to construct ExecutionEngine: " << errStr << "\n";
		return 1;
	}

	std::vector<GenericValue> noArgs;
	GenericValue result = execEngine->runFunction(mainFunc, noArgs);

	std::cout << "Result: " << result.IntVal.getLimitedValue() << "\n";
	return 0;
}
