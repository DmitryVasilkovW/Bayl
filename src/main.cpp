#include <__config>

int main() {
	generateLLVM();
	module.print(llvm::outs(), nullptr);
	return 0;
}
