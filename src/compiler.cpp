#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/Module.h"

llvm::LLVMContext context;
llvm::IRBuilder<> builder(context);
llvm::Module module("MyLangModule", context);

llvm::Function *generateLLVM() {
	auto mainFuncType = llvm::FunctionType::get(builder.getInt32Ty(), false);
	auto mainFunc = llvm::Function::Create(mainFuncType, llvm::Function::ExternalLinkage, "main", module);

	auto entryBlock = llvm::BasicBlock::Create(context, "entry", mainFunc);
	builder.SetInsertPoint(entryBlock);

	auto x = builder.CreateAlloca(builder.getInt32Ty(), nullptr, "x");
	builder.CreateStore(builder.getInt32(42), x);

	auto y = builder.CreateAlloca(builder.getInt32Ty(), nullptr, "y");
	auto xVal = builder.CreateLoad(builder.getInt32Ty(), x, "load_x");
	auto result = builder.CreateMul(xVal, builder.getInt32(2), "mul_tmp");
	builder.CreateStore(result, y);

	auto yVal = builder.CreateLoad(builder.getInt32Ty(), y, "load_y");
	builder.CreateCall(module.getFunction("printf"), {yVal});

	builder.CreateRet(builder.getInt32(0));

	return mainFunc;
}
