package org.bayl.ast.statement;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.WHILE;
import org.bayl.model.SourcePosition;

public class WhileNode extends Node {

    private final Node testCondition;
    private final Node loopBody;

    public WhileNode(SourcePosition pos, Node testCondition, Node loopBody) {
        super(pos);
        this.testCondition = testCondition;
        this.loopBody = loopBody;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("while ");
        sb.append(testCondition);
        sb.append(' ');
        sb.append(loopBody);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                WHILE.toString()
        ));

        testCondition.generateCode(bytecode);
        loopBody.generateCode(bytecode);
    }
}
