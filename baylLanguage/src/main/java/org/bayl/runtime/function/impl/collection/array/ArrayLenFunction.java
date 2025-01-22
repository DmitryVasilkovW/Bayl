package org.bayl.runtime.function.impl.collection.array;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;

public class ArrayLenFunction extends BaylFunction {

    private static final String NAME = "`array";
    private final String[] parameters = {NAME};

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
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
