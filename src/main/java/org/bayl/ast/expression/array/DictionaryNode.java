package org.bayl.ast.expression.array;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.Dictionary;
import org.bayl.runtime.DictionaryEntry;
import org.bayl.runtime.ZemObject;

public class DictionaryNode extends Node {
    private List<DictionaryEntryNode> elements;

    public DictionaryNode(SourcePosition pos, List<DictionaryEntryNode> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        Map<ZemObject, ZemObject> entries = new LinkedHashMap<ZemObject, ZemObject>(elements.size());
        for (DictionaryEntryNode node : elements) {
            DictionaryEntry entry = (DictionaryEntry) node.eval(interpreter);
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
}
