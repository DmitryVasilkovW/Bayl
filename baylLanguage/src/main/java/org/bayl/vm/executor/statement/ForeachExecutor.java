package org.bayl.vm.executor.statement;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.Dictionary;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.array.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class ForeachExecutor extends Executor {

    private final VariableExecutor onVariableExecutor;
    private final Executor asExecutor;
    private final Executor loopBody;

    public ForeachExecutor(
            SourcePosition pos, VariableExecutor onVariableExecutor, Executor asExecutor, Executor loopBody) {
        super(pos);
        this.onVariableExecutor = onVariableExecutor;
        this.asExecutor = asExecutor;
        this.loopBody = loopBody;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylObject onVariable =
                virtualMachine.getVariable(onVariableExecutor.getName(), onVariableExecutor.getPosition());
        BaylObject ret = null;
        if (onVariable instanceof BaylArray) {
            String asVariableName = asExecutor.toString();
            for (BaylObject element : (BaylArray) onVariable) {
                virtualMachine.setVariable(asVariableName, element);
                ret = loopBody.eval(virtualMachine);
            }
            return ret;
        } else if (onVariable instanceof Dictionary) {
            DictionaryEntryExecutor entryExecutor = (DictionaryEntryExecutor) asExecutor;
            String keyName = ((VariableExecutor) entryExecutor.getKey()).getName();
            String valueName = ((VariableExecutor) entryExecutor.getValue()).getName();
            for (Map.Entry<BaylObject, BaylObject> entry : (Dictionary) onVariable) {
                virtualMachine.setVariable(keyName, entry.getKey());
                virtualMachine.setVariable(valueName, entry.getValue());
                ret = loopBody.eval(virtualMachine);
            }
            return ret;
        }
        throw new InvalidTypeException("foreach expects an array or dictionary.", onVariableExecutor.getPosition());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("foreach ");
        sb.append(onVariableExecutor);
        sb.append(' ');
        sb.append(asExecutor);
        sb.append(' ');
        sb.append(loopBody);
        sb.append(')');
        return sb.toString();
    }
}
