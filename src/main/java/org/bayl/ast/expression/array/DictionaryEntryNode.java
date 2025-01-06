package org.bayl.ast.expression.array;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.DictionaryEntry;
import org.bayl.runtime.BaylObject;

public class DictionaryEntryNode extends Node {
    private Node key;
    private Node value;

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
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
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

        bytecode.add("DICT_ENTRY");
    }
}
