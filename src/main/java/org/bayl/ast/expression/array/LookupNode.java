package org.bayl.ast.expression.array;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.Dictionary;
import org.bayl.vm.impl.VirtualMachineImpl;

public class LookupNode extends Node {

    private final VariableNode varNode;
    private final Node keyNode;

    public LookupNode(SourcePosition pos, VariableNode varNode, Node keyNode) {
        super(pos);
        this.varNode = varNode;
        this.keyNode = keyNode;
    }

    public BaylObject get(VirtualMachineImpl interpreter) {
        BaylObject var = interpreter.getVariable(varNode.getName(), varNode.getPosition());
        BaylObject ret = null;
        if (var instanceof BaylArray) {
            int index = keyNode.eval(interpreter).toNumber(keyNode.getPosition()).intValue();
            return ((BaylArray) var).get(index);
        } else if (var instanceof Dictionary) {
            BaylObject key = keyNode.eval(interpreter);
            return ((Dictionary) var).get(key);
        }
        throw new InvalidTypeException("lookup expects an array or dictionary.", getPosition());
    }

    public void set(VirtualMachineImpl interpreter, BaylObject result) {
        BaylObject var = interpreter.getVariable(varNode.getName(), varNode.getPosition());
        BaylObject ret = null;
        if (var instanceof BaylArray) {
            int index = keyNode.eval(interpreter).toNumber(keyNode.getPosition()).intValue();
            ((BaylArray) var).set(index, result);
            return;
        } else if (var instanceof Dictionary) {
            BaylObject key = keyNode.eval(interpreter);
            ((Dictionary) var).set(key, result);
            return;
        }
        throw new InvalidTypeException("lookup expects an array or dictionary.", getPosition());
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return get(virtualMachine);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        sb.append("lookup ");
        sb.append(varNode);
        sb.append(' ');
        sb.append(keyNode);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("LOAD " + varNode.getName());

        keyNode.generateCode(bytecode);

        bytecode.add("LOOKUP");
    }

}
