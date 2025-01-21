package org.bayl.ast.expression.collection;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.DictionaryEntry;
import org.bayl.vm.Environment;

public class DictionaryEntryNode extends Node {

    private final Node key;
    private final Node value;

    public DictionaryEntryNode(SourcePosition pos, Node key, Node value) {
        super(pos);
        this.key = key;
        this.value = value;
    }

    public Node getKey() {
        return key;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return new DictionaryEntry(key.eval(virtualMachine), value.eval(virtualMachine));
    }

    @Override
    public String toString() {
        return "(" + key.toString() + " " + value.toString() + ")";
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        key.generateCode(bytecode);
        value.generateCode(bytecode);
    }
}
