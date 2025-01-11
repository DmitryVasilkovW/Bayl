package org.bayl.vm.executor.expression.array;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.DictionaryEntry;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class DictionaryEntryExecutor extends Executor {

    private final Executor key;
    private final Executor value;

    public DictionaryEntryExecutor(SourcePosition pos, Executor key, Executor value) {
        super(pos);
        this.key = key;
        this.value = value;
    }

    public Executor getKey() {
        return key;
    }

    public Executor getValue() {
        return value;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return new DictionaryEntry(key.eval(virtualMachine), value.eval(virtualMachine));
    }

    @Override
    public String toString() {
        return "(" + key.toString() + " " + value.toString() + ")";
    }
}
