package org.bayl.ast.expression.literale;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.PUSH_F;
import org.bayl.model.SourcePosition;

public class FalseNode extends Node {

    private static final String VALUE = "false";

    public FalseNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public String toString() {
        return VALUE;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                PUSH_F.toString(),
                VALUE)
        );
    }
}
