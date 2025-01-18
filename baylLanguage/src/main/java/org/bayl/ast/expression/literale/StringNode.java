package org.bayl.ast.expression.literale;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import static org.bayl.model.BytecodeToken.PUSH_S;
import org.bayl.vm.Environment;

public class StringNode extends Node {

    private final BaylString literal;

    public StringNode(SourcePosition pos, String literal) {
        super(pos);
        this.literal = new BaylString(literal);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return literal;
    }

    @Override
    public String toString() {
        return '"' + literal.toString() + '"';
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                PUSH_S.toString(),
                literal.toString()
        ));
    }
}
