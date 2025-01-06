package org.bayl.ast.expression.array;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylArray;
import java.util.ArrayList;
import java.util.List;

public class ArrayNode extends Node {

    private final List<Node> elements;

    public ArrayNode(SourcePosition pos, List<Node> elements) {
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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("ARRAY_INIT " + elements.size());

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).generateCode(bytecode);
            bytecode.add("ARRAY_STORE " + i);
        }
    }
}
