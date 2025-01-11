package org.bayl.runtime.function;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.List;

public class UserFunctionTMP extends Function {
    private final List<Parameter> parameters;
    private final Executor body;

    public UserFunctionTMP(List<Parameter> parameters, Executor body) {
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
    public BaylObject eval(VirtualMachineImpl interpreter, SourcePosition pos) {
        try {
            return body.eval(interpreter);
        } catch (ReturnException e) {
            return e.getReturn();
        }
    }
}
