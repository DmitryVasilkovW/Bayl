package org.bayl.runtime.function.impl;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.ref.Dictionary;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.runtime.object.value.BaylString;
import org.bayl.vm.Environment;

public class LenFunction extends BaylFunction {

    private static final String VALUE = "`obj";

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
        return VALUE;
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylObject obj = interpreter.getVariable(VALUE, pos);
        int len;
        if (obj instanceof BaylString) {
            len = obj
                    .toBaylString()
                    .toString()
                    .length();
        } else if (obj instanceof BaylArray) {
            len = ((BaylArray) obj).size();
        } else {
            len = ((Dictionary) obj).size();
        }

        return new BaylNumber(len);
    }
}
