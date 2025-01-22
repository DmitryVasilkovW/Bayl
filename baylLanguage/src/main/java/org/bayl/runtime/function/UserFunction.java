package org.bayl.runtime.function;

import java.util.List;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class UserFunction extends BaylFunction {

    private final List<Parameter> parameters;
    private final Executor body;

    public UserFunction(List<Parameter> parameters, Executor body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Executor getBody() {
        return body;
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String getParameterName(int index) {
        return parameters.get(index).name();
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return parameters.get(index).value();
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        try {
            return body.eval((interpreter));
        } catch (ReturnException e) {
            return e.getReturn();
        }
    }
}
