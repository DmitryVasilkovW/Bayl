package org.bayl.ast.statement;

import org.bayl.model.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.collection.LookupNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import static org.bayl.model.BytecodeToken.SET;
import org.bayl.vm.Environment;

public class AssignNode extends BinaryOpNode {

    private static final String EXCEPTION_MESSAGE = "Left hand of assignment must be a variable.";

    public AssignNode(SourcePosition pos, Node varNode, Node expression) {
        super(pos, "set!", varNode, expression);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        Node left = getLeft();
        BaylObject value = getRight().eval(virtualMachine);
        if (left instanceof VariableNode) {
            String name = ((VariableNode) left).getName();
            virtualMachine.setVariable(name, value);
            return value;
        } else if (left instanceof LookupNode) {
            ((LookupNode) left).set(virtualMachine, value);
            return value;
        }
        throw new InvalidTypeException(EXCEPTION_MESSAGE, left.getPosition());
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
