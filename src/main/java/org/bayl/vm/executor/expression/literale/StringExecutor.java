package org.bayl.vm.executor.expression.literale;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class StringExecutor extends Executor {

    private final BaylString literal;

    public StringExecutor(SourcePosition pos, String literal) {
        super(pos);
        this.literal = new BaylString(literal);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return literal;
    }

    @Override
    public String toString() {
        return '"' + literal.toString() + '"';
    }
}
