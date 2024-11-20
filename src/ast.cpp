struct ASTNode {
	virtual ~ASTNode() = default;
};

struct BinaryOpNode : ASTNode {
	std::string op;
	ASTNode *left;
	ASTNode *right;
};

struct NumberNode : ASTNode {
	int value;
};
