package org.bayl.ast.expression.function;

import java.util.ArrayList;
import java.util.List;

import org.bayl.Interpreter;
import org.bayl.InvalidTypeException;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.Function;
import org.bayl.runtime.ZemObject;

public class FunctionCallNode extends Node {
    final static public List<Node> NO_ARGUMENTS = new ArrayList<Node>(0);

    private Node functionNode;
    private List<Node> arguments;

    public FunctionCallNode(SourcePosition pos, Node functionNode, List<Node> arguments) {
        super(pos);
        this.functionNode = functionNode;
        this.arguments = arguments;
    }

    private String getFunctionName() {
        if (functionNode instanceof VariableNode) {
            return ((VariableNode) functionNode).getName();
        }
        return null;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemObject expression = functionNode.eval(interpreter);
        if (!(expression instanceof Function)) {
            throw new InvalidTypeException("Call to invalid function", getPosition());
        }
        Function function = (Function) expression;
        List<ZemObject> args = new ArrayList<ZemObject>(arguments.size());
        for (Node node : arguments) {
            args.add(node.eval(interpreter));
        }
        return interpreter.callFunction(function, args, getPosition(), getFunctionName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        String functionName = getFunctionName();
        if (functionName == null) {
            functionName = functionNode.toString();
        }
        sb.append(functionName);
        for (Node arg : arguments) {
            sb.append(' ');
            sb.append(arg);
        }
        sb.append(')');
        return sb.toString();
    }
}
