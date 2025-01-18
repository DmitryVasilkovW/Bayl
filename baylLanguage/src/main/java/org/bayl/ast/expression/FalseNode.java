package org.bayl.ast.expression;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import static org.bayl.model.BytecodeToken.PUSH_F;
import org.bayl.vm.Environment;

public class FalseNode extends Node {

    private static final String VALUE = "false";

    public FalseNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return BaylBoolean.FALSE;
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
