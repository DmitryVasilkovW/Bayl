package org.bayl.ast.expression.collection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.DICT_END;
import static org.bayl.model.BytecodeToken.DICT_INIT;
import static org.bayl.model.BytecodeToken.DICT_PAIR;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.Dictionary;
import org.bayl.runtime.object.ref.DictionaryEntry;
import org.bayl.vm.Environment;

public class DictionaryNode extends Node {

    private final List<DictionaryEntryNode> elements;

    public DictionaryNode(SourcePosition pos, List<DictionaryEntryNode> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        Map<BaylObject, BaylObject> entries = new LinkedHashMap<BaylObject, BaylObject>(elements.size());
        for (DictionaryEntryNode node : elements) {
            DictionaryEntry entry = (DictionaryEntry) node.eval(virtualMachine);
            entries.put(entry.getKey(), entry.getValue());
        }
        return new Dictionary(entries);
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
            String lineForEntry = getBytecodeLine(
                    DICT_PAIR.toString(),
                    entry.getPositionForBytecode()
            );

            bytecode.add(lineForEntry);
            entry.generateCode(bytecode);
        }

        bytecode.add(DICT_END.toString());
    }
}
