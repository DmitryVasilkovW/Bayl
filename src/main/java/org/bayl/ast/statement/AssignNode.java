package org.bayl.ast.statement;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.InvalidTypeException;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.expression.array.LookupNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.ZemObject;

public class AssignNode extends BinaryOpNode {
    public AssignNode(SourcePosition pos, Node var, Node expression) {
        super(pos, "set!", var, expression);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        Node left = getLeft();
        ZemObject value = getRight().eval(interpreter);
        if (left instanceof VariableNode) {
            String name = ((VariableNode) left).getName();
            interpreter.setVariable(name, value);
            return value;
        } else if (left instanceof LookupNode) {
            ((LookupNode) left).set(interpreter, value);
            return value;
        }
        throw new InvalidTypeException("Left hand of assignment must be a variable.", left.getPosition());
    }
}
