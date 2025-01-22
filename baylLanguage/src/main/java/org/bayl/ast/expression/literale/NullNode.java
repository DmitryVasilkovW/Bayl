package org.bayl.ast.expression.literale;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.PUSH_NULL;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylNull;
import org.bayl.vm.Environment;

public class NullNode extends Node {

    private static final String VALUE = "null";

    public NullNode(SourcePosition position) {
        super(position);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return new BaylNull();
    }

    @Override
    public String toString() {
        return VALUE;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(
                getBytecodeLineWithPosition(
                        PUSH_NULL.toString()
                )
        );
    }
}
