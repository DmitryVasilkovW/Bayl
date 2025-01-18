package org.bayl.vm.executor.expression.literale;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class TrueExecutor extends Executor {

    public TrueExecutor(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "true";
    }
}
