package org.bayl.vm.executor.expression.collection;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.BaylDictionaryEntry;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

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
    public BaylObject eval(Environment virtualMachine) {
        return new BaylDictionaryEntry(key.eval(virtualMachine), value.eval(virtualMachine));
    }

    @Override
    public String toString() {
        return "(" + key.toString() + " " + value.toString() + ")";
    }
}
