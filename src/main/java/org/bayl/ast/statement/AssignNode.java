package org.bayl.ast.statement;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.expression.array.LookupNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.BaylObject;

public class AssignNode extends BinaryOpNode {
    public AssignNode(SourcePosition pos, Node var, Node expression) {
        super(pos, "set!", var, expression);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
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
        throw new InvalidTypeException("Left hand of assignment must be a variable.", left.getPosition());
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getRight().generateCode(bytecode);
        Node left = getLeft();
        if (left instanceof VariableNode) {
            String name = ((VariableNode) left).getName();
            bytecode.add("STORE " + name);
        } else if (left instanceof LookupNode) {
            ((LookupNode) left).generateCode(bytecode);
            bytecode.add("STORE_LOOKUP");
        } else {
            throw new InvalidTypeException("Left hand of assignment must be a variable.", left.getPosition());
        }
    }
}
