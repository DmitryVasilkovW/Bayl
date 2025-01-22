package org.bayl.ast.statement;

import org.bayl.ast.Node;
import org.bayl.ast.expression.collection.DictionaryEntryNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.DICT_PAIR;
import static org.bayl.model.BytecodeToken.FOREACH;
import org.bayl.model.SourcePosition;

public class ForeachNode extends Node {

    private final VariableNode onVariableNode;
    private final Node asNode;
    private final Node loopBody;

    public ForeachNode(SourcePosition pos, VariableNode onVariableNode, Node asNode, Node loopBody) {
        super(pos);
        this.onVariableNode = onVariableNode;
        this.asNode = asNode;
        this.loopBody = loopBody;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("foreach ");
        sb.append(onVariableNode);
        sb.append(' ');
        sb.append(asNode);
        sb.append(' ');
        sb.append(loopBody);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                FOREACH.toString())
        );

        onVariableNode.generateCode(bytecode);
        if (asNode instanceof DictionaryEntryNode) {
            bytecode.add(DICT_PAIR + " " + asNode.getPositionForBytecode());
        }
        asNode.generateCode(bytecode);
        loopBody.generateCode(bytecode);
    }
}
