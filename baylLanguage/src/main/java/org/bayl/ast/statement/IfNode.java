package org.bayl.ast.statement;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.ELSE;
import static org.bayl.model.BytecodeToken.IF;
import org.bayl.model.SourcePosition;

public class IfNode extends Node {

    private final Node testCondition;
    private final Node thenBlock;
    private final Node elseBlock;

    public IfNode(SourcePosition pos, Node testCondition, Node thenBlock, Node elseBlock) {
        super(pos);
        this.testCondition = testCondition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("if ");
        sb.append(testCondition);
        sb.append(' ');
        sb.append(thenBlock);
        if (elseBlock != null) {
            sb.append(' ');
            sb.append(elseBlock);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                IF.toString()
        ));
        testCondition.generateCode(bytecode);
        thenBlock.generateCode(bytecode);

        if (elseBlock != null) {
            bytecode.add(ELSE.toString());
            elseBlock.generateCode(bytecode);
        }
    }
}
