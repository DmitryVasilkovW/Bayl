package org.bayl.vm.executor.expression.array;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylArray;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.ArrayList;
import java.util.List;

public class ArrayExecutor extends Executor {

    private final List<Node> elements;

    public ArrayExecutor(SourcePosition pos, List<Node> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        List<BaylObject> items = new ArrayList<BaylObject>(elements.size());
        for (Node node : elements) {
            items.add(node.eval(virtualMachine));
        }
        return new BaylArray(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'(");
        for (Node node : elements) {
            sb.append(node);
            sb.append(' ');
        }
        sb.append(')');
        return sb.toString();
    }
}
