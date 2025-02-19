package org.bayl.vm.executor.expression.collection;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class ArrayExecutor extends Executor {

    private final List<Executor> elements;

    public ArrayExecutor(SourcePosition pos, List<Executor> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        List<BaylObject> items = new ArrayList<BaylObject>(elements.size());
        for (Executor node : elements) {
            items.add(node.eval(virtualMachine));
        }
        return new BaylArray(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'(");
        for (Executor node : elements) {
            sb.append(node);
            sb.append(' ');
        }
        sb.append(')');
        return sb.toString();
    }
}
