package org.bayl.ast.expression.variable;

import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.LOAD;
import org.bayl.model.SourcePosition;

public class VariableNode extends Node {

    private final String name;

    public VariableNode(SourcePosition pos, String variableName) {
        super(pos);
        name = variableName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                LOAD.toString(),
                name)
        );
    }
}
