package org.bayl.vm;

import java.util.List;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;

public interface Environment {

    BaylObject getVariable(String name, SourcePosition pos);

    void setVariable(String name, BaylObject value);

    BaylObject callFunction(Function function, List<BaylObject> args, SourcePosition pos, String functionName);
}
