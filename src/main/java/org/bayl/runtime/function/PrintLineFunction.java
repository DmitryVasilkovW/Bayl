package org.bayl.runtime.function;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;

public class PrintLineFunction extends PrintFunction {
    @Override
    public BaylObject eval(VirtualMachineImpl interpreter, SourcePosition pos) {
        BaylString str = interpreter.getVariable("string", pos).toZString();
        System.out.println(str.toString());
        return str;
    }
}
