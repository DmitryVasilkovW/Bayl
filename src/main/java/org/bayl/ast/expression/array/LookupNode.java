package org.bayl.ast.expression.array;

import org.bayl.Interpreter;
import org.bayl.InvalidTypeException;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.Dictionary;
import org.bayl.runtime.ZemArray;
import org.bayl.runtime.ZemObject;

public class LookupNode extends Node {
    private VariableNode varNode;
    private Node keyNode;

    public LookupNode(SourcePosition pos, VariableNode varNode, Node keyNode) {
        super(pos);
        this.varNode = varNode;
        this.keyNode = keyNode;
    }

    public ZemObject get(Interpreter interpreter) {
        ZemObject var = interpreter.getVariable(varNode.getName(), varNode.getPosition());
        ZemObject ret = null;
        if (var instanceof ZemArray) {
            int index = keyNode.eval(interpreter).toNumber(keyNode.getPosition()).intValue();
            return ((ZemArray) var).get(index);
        } else if (var instanceof Dictionary) {
            ZemObject key = keyNode.eval(interpreter);
            return ((Dictionary) var).get(key);
        }
        throw new InvalidTypeException("lookup expects an array or dictionary.", getPosition());
    }

    public void set(Interpreter interpreter, ZemObject result) {
        ZemObject var = interpreter.getVariable(varNode.getName(), varNode.getPosition());
        ZemObject ret = null;
        if (var instanceof ZemArray) {
            int index = keyNode.eval(interpreter).toNumber(keyNode.getPosition()).intValue();
            ((ZemArray) var).set(index, result);
            return;
        } else if (var instanceof Dictionary) {
            ZemObject key = keyNode.eval(interpreter);
            ((Dictionary) var).set(key, result);
            return;
        }
        throw new InvalidTypeException("lookup expects an array or dictionary.", getPosition());
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return get(interpreter);
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
}
