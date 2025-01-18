package org.bayl.ast.expression.array;

import java.util.ArrayList;
import java.util.List;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import static org.bayl.model.BytecodeToken.ARRAY_END;
import static org.bayl.model.BytecodeToken.ARRAY_INIT;
import static org.bayl.model.BytecodeToken.ARRAY_STORE;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylArray;
import org.bayl.vm.Environment;

public class ArrayNode extends Node {

    private final List<Node> elements;

    public ArrayNode(SourcePosition pos, List<Node> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        var items = new ArrayList<BaylObject>(elements.size());
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
        bytecode.add(getBytecodeLineWithPosition(
                ARRAY_INIT.toString(),
                elements.size() + ""
        ));

        for (int i = 0; i < elements.size(); i++) {
            String indexLine = getBytecodeLine(
                    ARRAY_STORE.toString(),
                    i + ""
            );

            bytecode.add(indexLine);
            elements.get(i).generateCode(bytecode);
        }
        bytecode.add(ARRAY_END.toString());
    }
}
