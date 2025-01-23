package org.bayl.runtime.function.impl.math;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;

public class MinFunction extends BaylFunction {

    private static final String LEFT = "`minL";
    private static final String RIGHT = "`minR";
    private final String[] param = {LEFT, RIGHT};

    @Override
    public int getParameterCount() {
        return 2;
    }

    @Override
    public String getParameterName(int index) {
        return param[index];
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylNumber left = (BaylNumber) interpreter.getVariable(LEFT, pos);
        BaylNumber right = (BaylNumber) interpreter.getVariable(RIGHT, pos);

        return left.min(right);
    }
}
