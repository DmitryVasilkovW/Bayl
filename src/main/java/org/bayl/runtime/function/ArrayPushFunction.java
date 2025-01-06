package org.bayl.runtime.function;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.runtime.Function;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.BaylObject;

public class ArrayPushFunction extends Function {
    private final String[] parameters = {"array", "element"};

    @Override
    public BaylObject eval(VirtualMachineImpl interpreter, SourcePosition pos) {
        BaylArray array = (BaylArray) interpreter.getVariable("array", pos);
        array.push(interpreter.getVariable("element", pos));
        return array;
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 2;
    }

    @Override
    public String getParameterName(int index) {
        return parameters[index];
    }

}
