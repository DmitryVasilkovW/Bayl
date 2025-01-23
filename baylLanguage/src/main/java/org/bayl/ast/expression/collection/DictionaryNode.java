package org.bayl.ast.expression.collection;

import java.util.List;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.DICT_END;
import static org.bayl.model.BytecodeToken.DICT_INIT;
import org.bayl.model.SourcePosition;

public class DictionaryNode extends Node {

    private final List<DictionaryEntryNode> elements;

    public DictionaryNode(SourcePosition pos, List<DictionaryEntryNode> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(dict ");
        for (DictionaryEntryNode node : elements) {
            sb.append(node);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                DICT_INIT.toString(),
                elements.size() + ""
        ));

        for (DictionaryEntryNode entry : elements) {
            entry.generateCode(bytecode);
        }

        bytecode.add(DICT_END.toString());
    }
}
