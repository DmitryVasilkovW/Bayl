package org.bayl.runtime;

import org.bayl.SourcePosition;
import org.bayl.vm.Environment;

public abstract class BaylClass extends BaylObject {

    public abstract BaylObject eval(Environment virtualMachine, SourcePosition pos);

    public abstract void setCall(String name, Environment virtualMachine, SourcePosition pos);

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }
}
