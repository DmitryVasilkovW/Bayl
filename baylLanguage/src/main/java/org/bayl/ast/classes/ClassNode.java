package org.bayl.ast.classes;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.control.BlockNode;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.classes.UserClass;
import org.bayl.vm.Environment;

public class ClassNode extends Node {

    private final BlockNode body;

    public ClassNode(SourcePosition position, BlockNode body) {
        super(position);
        this.body = body;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return new UserClass(body);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("CLASS");
    }
}
