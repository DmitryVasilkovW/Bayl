package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemNumber;
import org.bayl.runtime.ZemObject;

public class NumberNode extends Node {
    private ZemNumber number;

    public NumberNode(SourcePosition pos, String number) {
        super(pos);
        this.number = new ZemNumber(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return number;
    }
}
