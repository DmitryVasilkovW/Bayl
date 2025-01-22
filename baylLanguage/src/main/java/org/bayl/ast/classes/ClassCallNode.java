package org.bayl.ast.classes;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.CLASS_CALL;
import org.bayl.model.SourcePosition;

public class ClassCallNode extends Node {

    private final Node classNode;
    private final Node attribute;
    private final String name;

    public ClassCallNode(SourcePosition position, Node classNode, Node attribute, String name) {
        super(position);
        this.classNode = classNode;
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(
                getBytecodeLineWithPosition(
                        CLASS_CALL.toString(),
                        name
                )
        );
        classNode.generateCode(bytecode);
        attribute.generateCode(bytecode);
    }
}
