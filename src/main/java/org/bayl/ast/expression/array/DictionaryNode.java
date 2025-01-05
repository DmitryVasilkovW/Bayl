package org.bayl.ast.expression.array;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.Dictionary;
import org.bayl.runtime.object.DictionaryEntry;
import org.bayl.runtime.BaylObject;

public class DictionaryNode extends Node {
    private List<DictionaryEntryNode> elements;

    public DictionaryNode(SourcePosition pos, List<DictionaryEntryNode> elements) {
        super(pos);
        this.elements = elements;
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        Map<BaylObject, BaylObject> entries = new LinkedHashMap<BaylObject, BaylObject>(elements.size());
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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("DICT_INIT");

        for (DictionaryEntryNode entry : elements) {
            entry.generateCode(bytecode);
            bytecode.add("DICT_ADD");
        }
    }

}
