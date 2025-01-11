package org.bayl.vm.executor.expression.array;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.Dictionary;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class LookupExecutor extends Executor {

    private final VariableExecutor variableExecutor;
    private final Executor keyExecutor;

    public LookupExecutor(SourcePosition pos, VariableExecutor varNode, Executor keyNode) {
        super(pos);
        this.variableExecutor = varNode;
        this.keyExecutor = keyNode;
    }

    public BaylObject get(VirtualMachineImpl interpreter) {
        BaylObject var = interpreter.getVariable(variableExecutor.getName(), variableExecutor.getPosition());
        BaylObject ret = null;
        if (var instanceof BaylArray) {
            int index = keyExecutor.eval(interpreter).toNumber(keyExecutor.getPosition()).intValue();
            return ((BaylArray) var).get(index);
        } else if (var instanceof Dictionary) {
            BaylObject key = keyExecutor.eval(interpreter);
            return ((Dictionary) var).get(key);
        }
        throw new InvalidTypeException("lookup expects an array or dictionary.", getPosition());
    }

    public void set(VirtualMachineImpl interpreter, BaylObject result) {
        BaylObject var = interpreter.getVariable(variableExecutor.getName(), variableExecutor.getPosition());
        BaylObject ret = null;
        if (var instanceof BaylArray) {
            int index = keyExecutor.eval(interpreter).toNumber(keyExecutor.getPosition()).intValue();
            ((BaylArray) var).set(index, result);
            return;
        } else if (var instanceof Dictionary) {
            BaylObject key = keyExecutor.eval(interpreter);
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
        sb.append(variableExecutor);
        sb.append(' ');
        sb.append(keyExecutor);
        sb.append(')');
        return sb.toString();
    }
}
