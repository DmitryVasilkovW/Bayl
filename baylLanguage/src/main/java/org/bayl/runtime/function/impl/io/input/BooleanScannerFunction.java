package org.bayl.runtime.function.impl.io.input;

import java.util.Scanner;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;

public class BooleanScannerFunction extends BaylFunction {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public String getParameterName(int index) {
        return null;
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        boolean bool = scanner.nextBoolean();
        return BaylBoolean.valueOf(bool);
    }
}
