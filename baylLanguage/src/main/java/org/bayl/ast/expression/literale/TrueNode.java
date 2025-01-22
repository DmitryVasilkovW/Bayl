package org.bayl.ast.expression.literale;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import static org.bayl.model.BytecodeToken.PUSH_T;
import org.bayl.vm.Environment;

public class TrueNode extends Node {

    public static final String VALUE = "true";

    public TrueNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return BaylBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                PUSH_T.toString(),
                VALUE)
        );
    }
}
