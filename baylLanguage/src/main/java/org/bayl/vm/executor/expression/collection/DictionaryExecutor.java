package org.bayl.vm.executor.expression.collection;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.Dictionary;
import org.bayl.runtime.object.DictionaryEntry;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class DictionaryExecutor extends Executor {

    private final List<DictionaryEntryExecutor> elements;

    public DictionaryExecutor(SourcePosition pos, List<DictionaryEntryExecutor> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        Map<BaylObject, BaylObject> entries = new LinkedHashMap<BaylObject, BaylObject>(elements.size());
        for (DictionaryEntryExecutor node : elements) {
            DictionaryEntry entry = (DictionaryEntry) node.eval(virtualMachine);
            entries.put(entry.getKey(), entry.getValue());
        }
        return new Dictionary(entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(dict ");
        for (DictionaryEntryExecutor node : elements) {
            sb.append(node);
        }
        sb.append(")");
        return sb.toString();
    }
}
