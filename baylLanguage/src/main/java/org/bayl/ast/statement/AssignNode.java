package org.bayl.ast.statement;

import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.collection.LookupNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.SET;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.exception.InvalidTypeException;

public class AssignNode extends BinaryOpNode {

    private static final String EXCEPTION_MESSAGE = "Left hand of assignment must be a variable.";

    public AssignNode(SourcePosition pos, Node varNode, Node expression) {
        super(pos, "set!", varNode, expression);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(SET.toString()));

        getRight().generateCode(bytecode);
        getLeft().generateCode(bytecode);

        Node left = getLeft();
        if (!validateLeft(left)) {
            throw new InvalidTypeException(EXCEPTION_MESSAGE, left.getPosition());
        }
    }

    private boolean validateLeft(Node left) {
        return left instanceof VariableNode || left instanceof LookupNode;
    }
}
