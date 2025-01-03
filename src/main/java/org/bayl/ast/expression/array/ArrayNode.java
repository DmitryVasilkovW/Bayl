package org.bayl.ast.expression.array;

import java.util.ArrayList;
import java.util.List;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemArray;
import org.bayl.runtime.ZemObject;

public class ArrayNode extends Node {
    private List<Node> elements;

    public ArrayNode(SourcePosition pos, List<Node> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        List<ZemObject> items = new ArrayList<ZemObject>(elements.size());
        for (Node node : elements) {
            items.add(node.eval(interpreter));
        }
        return new ZemArray(items);
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
