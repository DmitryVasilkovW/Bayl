package org.bayl.runtime;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;

public abstract class Function extends ZemObject {

    abstract public int getParameterCount();

    abstract public String getParameterName(int index);

    abstract public ZemObject getDefaultValue(int index);

    abstract public ZemObject eval(Interpreter interpreter, SourcePosition pos);

    @Override
    public int compareTo(ZemObject o) {
        throw new UnsupportedOperationException();
    }
}
