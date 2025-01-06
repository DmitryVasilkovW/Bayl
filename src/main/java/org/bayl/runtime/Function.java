package org.bayl.runtime;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;

public abstract class Function extends BaylObject {

    abstract public int getParameterCount();

    abstract public String getParameterName(int index);

    abstract public BaylObject getDefaultValue(int index);

    abstract public BaylObject eval(VirtualMachineImpl interpreter, SourcePosition pos);

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }
}
