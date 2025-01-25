package org.bayl.ast.classes;

import org.bayl.ast.Node;
import org.bayl.ast.control.BlockNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.CLASS;
import org.bayl.model.SourcePosition;

public class ClassNode extends Node {

    private final BlockNode body;

    public ClassNode(SourcePosition position, BlockNode body) {
        super(position);
        this.body = body;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(
                getBytecodeLineWithPosition(CLASS.toString())
        );
        body.generateCode(bytecode);
    }
}
