package org.bayl.runtime;

import org.bayl.model.SourcePosition;
import org.bayl.vm.Environment;

public abstract class BaylFunction extends BaylObject {

    abstract public int getParameterCount();

    abstract public String getParameterName(int index);

    abstract public BaylObject getDefaultValue(int index);

    abstract public BaylObject eval(Environment interpreter, SourcePosition pos);

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaylObject clone() {
        return this;
    }
}
