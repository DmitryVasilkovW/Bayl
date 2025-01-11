package org.bayl.vm.executor.expression.function;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.ArrayList;
import java.util.List;

public class FunctionCallExecutor extends Executor {

    final static public List<Executor> NO_ARGUMENTS = new ArrayList<Executor>(0);
    private final Executor functionExecutor;
    private final List<Executor> arguments;

    public FunctionCallExecutor(SourcePosition pos, Executor functionExecutor, List<Executor> arguments) {
        super(pos);
        this.functionExecutor = functionExecutor;
        this.arguments = arguments;
    }

    private String getFunctionName() {
        if (functionExecutor instanceof VariableExecutor) {
            return ((VariableExecutor) functionExecutor).getName();
        }
        return null;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylObject expression = functionExecutor.eval(virtualMachine);
        if (!(expression instanceof Function)) {
            throw new InvalidTypeException("Call to invalid function", getPosition());
        }
        Function function = (Function) expression;
        List<BaylObject> args = new ArrayList<BaylObject>(arguments.size());
        for (Executor Executor : arguments) {
            args.add(Executor.eval(virtualMachine));
        }
        return virtualMachine.callFunction(function, args, getPosition(), getFunctionName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        String functionName = getFunctionName();
        if (functionName == null) {
            functionName = functionExecutor.toString();
        }
        sb.append(functionName);
        for (Executor arg : arguments) {
            sb.append(' ');
            sb.append(arg);
        }
        sb.append(')');
        return sb.toString();
    }
}
