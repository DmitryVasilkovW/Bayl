package org.bayl.vm.executor.expression.function;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.function.UserFunction;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FunctionExecutor extends Executor {

    private final List<Executor> parameters;
    private final Executor body;

    public FunctionExecutor(SourcePosition pos, List<Executor> parameters, Executor body) {
        super(pos);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        List<Parameter> params = new ArrayList<Parameter>(parameters.size());
        for (Executor Executor : parameters) {
            // TODO clean up getting parameters
            String parameterName;
            BaylObject parameterValue;
            if (Executor instanceof VariableExecutor) {
                parameterName = ((VariableExecutor) Executor).getName();
                parameterValue = null;
            } else if (Executor instanceof AssignExecutor) {
                parameterName = ((VariableExecutor) ((AssignExecutor) Executor).getLeft()).getName();
                parameterValue = ((AssignExecutor) Executor).getRight().eval(virtualMachine);
            } else {
                throw new RuntimeException("Invalid function");
            }
            Parameter param = new Parameter(parameterName, parameterValue);
            params.add(param);
        }
        return new UserFunction(params, body);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(function (");
        boolean first = true;
        for (Executor Executor : parameters) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(Executor);
        }
        sb.append(") ");
        sb.append(body);
        sb.append(')');
        return sb.toString();
    }

}
