package org.bayl.vm.executor.expression.function;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.ast.statement.AssignNode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.function.UserFunction;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.ArrayList;
import java.util.List;

public class FunctionExecutor extends Executor {

    final static public List<Node> NO_PARAMETERS = new ArrayList<Node>(0);

    private final List<Node> parameters;
    private final Node body;

    public FunctionExecutor(SourcePosition pos, List<Node> parameters, Node body) {
        super(pos);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        List<Parameter> params = new ArrayList<Parameter>(parameters.size());
        for (Node node : parameters) {
            // TODO clean up getting parameters
            String parameterName;
            BaylObject parameterValue;
            if (node instanceof VariableNode) {
                parameterName = ((VariableNode) node).getName();
                parameterValue = null;
            } else if (node instanceof AssignNode) {
                parameterName = ((VariableNode) ((AssignNode) node).getLeft()).getName();
                parameterValue = ((AssignNode) node).getRight().eval(virtualMachine);
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
