package org.bayl.ast.expression.literale;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;

public class NumberNode extends Node {
    private BaylNumber number;

    public NumberNode(SourcePosition pos, String number) {
        super(pos);
        this.number = new BaylNumber(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return number;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("PUSH " +  number.toString());
    }
}
