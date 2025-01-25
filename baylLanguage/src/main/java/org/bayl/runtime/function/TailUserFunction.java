package org.bayl.runtime.function;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

import java.util.List;

public class TailUserFunction extends UserFunction {
    public TailUserFunction(List<Parameter> parameters, Executor body) {
        super(parameters, body);
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        if (!VirtualMachineImpl.isJitEnabled()) {
            try {
                return body.eval(interpreter);
            } catch (ReturnException e) {
                return e.getReturn();
            }
        }

        try {
            var functionCallExecutor = (FunctionCallExecutor) ((ReturnExecutor)
                    ((BlockExecutor) body).getStatements().removeLast()).getExpression();
            var arguments = functionCallExecutor.getArguments();

            while (true) {
                body.eval(interpreter);
                arguments.forEach(argument -> argument.eval(interpreter));
            }
        } catch (ReturnException e) {
            return e.getReturn();
        }

    }
}
