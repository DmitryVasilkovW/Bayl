package org.bayl.runtime.function.impl.literal;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.runtime.object.value.BaylNull;
import org.bayl.vm.Environment;

public class IsNullFunction extends BaylFunction {

    private static final String NAME = "`object";

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String getParameterName(int index) {
        return NAME;
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylObject value = interpreter.getVariable(NAME, pos);
        return BaylBoolean.valueOf(value instanceof BaylNull);
    }
}
