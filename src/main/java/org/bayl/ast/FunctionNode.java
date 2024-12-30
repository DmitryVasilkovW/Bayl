package org.bayl.ast;

import java.util.ArrayList;
import java.util.List;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.UserFunction;
import org.bayl.runtime.ZemObject;

public class FunctionNode extends Node {
    final static public List<Node> NO_PARAMETERS = new ArrayList<Node>(0);

    private List<Node> parameters;
    private Node body;

    public FunctionNode(SourcePosition pos, List<Node> parameters, Node body) {
        super(pos);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        List<Parameter> params = new ArrayList<Parameter>(parameters.size());
        for (Node node : parameters) {
            // TODO clean up getting parameters
            String parameterName;
            ZemObject parameterValue;
            if (node instanceof VariableNode) {
                parameterName = ((VariableNode) node).getName();
                parameterValue = null;
            } else if (node instanceof AssignNode) {
                parameterName = ((VariableNode) ((AssignNode) node).getLeft()).getName();
                parameterValue = ((AssignNode) node).getRight().eval(interpreter);
            } else {
                throw new RuntimeException("Invalid function");
            }
            Parameter param = new Parameter(parameterName, parameterValue);
            params.add(param);
        }
        return new UserFunction(params, body);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(function (");
        boolean first = true;
        for (Node node : parameters) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(node);
        }
        sb.append(") ");
        sb.append(body);
        sb.append(')');
        return sb.toString();
    }
}
