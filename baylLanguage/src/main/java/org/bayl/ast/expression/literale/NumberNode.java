package org.bayl.ast.expression.literale;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import static org.bayl.model.BytecodeToken.PUSH_N;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.Environment;

public class NumberNode extends Node {

    private final BaylNumber number;

    public NumberNode(SourcePosition pos, String number) {
        super(pos);
        this.number = new BaylNumber(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return number;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                PUSH_N.toString(),
                number.toString()
        ));
    }
}
