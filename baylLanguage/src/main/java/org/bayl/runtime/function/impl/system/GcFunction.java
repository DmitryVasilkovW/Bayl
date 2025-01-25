package org.bayl.runtime.function.impl.system;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.impl.VirtualMachineImpl;

public class GcFunction extends BaylFunction {

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public String getParameterName(int index) {
        return null;
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        if (interpreter instanceof VirtualMachineImpl) {
            ((VirtualMachineImpl) interpreter).gc();
            return BaylBoolean.valueOf(true);
        }

        return BaylBoolean.valueOf(false);
    }
}
