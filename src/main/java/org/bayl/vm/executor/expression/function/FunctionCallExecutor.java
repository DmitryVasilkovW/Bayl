package org.bayl.vm.executor.expression.function;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.ArrayList;
import java.util.List;

public class FunctionCallExecutor extends Executor {

    final static public List<Node> NO_ARGUMENTS = new ArrayList<Node>(0);
    private final Node functionNode;
    private final List<Node> arguments;

    public FunctionCallExecutor(SourcePosition pos, Node functionNode, List<Node> arguments) {
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
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylObject expression = functionNode.eval(virtualMachine);
        if (!(expression instanceof Function)) {
            throw new InvalidTypeException("Call to invalid function", getPosition());
        }
        Function function = (Function) expression;
        List<BaylObject> args = new ArrayList<BaylObject>(arguments.size());
        for (Node node : arguments) {
            args.add(node.eval(virtualMachine));
        }
        return virtualMachine.callFunction(function, args, getPosition(), getFunctionName());
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
