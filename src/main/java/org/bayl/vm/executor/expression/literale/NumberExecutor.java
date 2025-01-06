package org.bayl.vm.executor.expression.literale;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class NumberExecutor extends Executor {

    private final BaylNumber number;

    public NumberExecutor(SourcePosition pos, String number) {
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
}
