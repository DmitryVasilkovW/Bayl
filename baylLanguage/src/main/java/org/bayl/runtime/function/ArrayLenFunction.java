package org.bayl.runtime.function;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.impl.VirtualMachineImpl;

public class ArrayLenFunction extends Function {

    private static final String NAME = "array";
    private final String[] parameters = {NAME};

    @Override
    public BaylObject eval(VirtualMachineImpl interpreter, SourcePosition pos) {
        BaylArray array = (BaylArray) interpreter.getVariable(NAME, pos);
        return new BaylNumber(array.size());
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String getParameterName(int index) {
        return parameters[index];
    }
}
