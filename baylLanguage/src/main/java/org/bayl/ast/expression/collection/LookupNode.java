package org.bayl.ast.expression.collection;

import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.LOOKUP;
import org.bayl.model.SourcePosition;

public class LookupNode extends Node {

    private final VariableNode varNode;
    private final Node keyNode;

    public LookupNode(SourcePosition pos, VariableNode varNode, Node keyNode) {
        super(pos);
        this.varNode = varNode;
        this.keyNode = keyNode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        sb.append("lookup ");
        sb.append(varNode);
        sb.append(' ');
        sb.append(keyNode);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                LOOKUP.toString()
        ));

        varNode.generateCode(bytecode);
        keyNode.generateCode(bytecode);
    }
}
