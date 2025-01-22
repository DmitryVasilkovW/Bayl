package org.bayl.runtime.function.impl.collection.array;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.vm.Environment;

public class ArrayPushFunction extends BaylFunction {

    private static final String KEY = "`array";
    private static final String VALUE = "`element";
    private final String[] parameters = {KEY, VALUE};

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylArray array = (BaylArray) interpreter.getVariable(KEY, pos);
        array.push(interpreter.getVariable(VALUE, pos));
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
