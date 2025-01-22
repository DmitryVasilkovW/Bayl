package org.bayl.vm.executor.classes;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.classes.UserClass;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;

public class ClassCallExecutor extends Executor {

    private final Executor classExecutor;
    private final Executor attribute;
    private final String name;

    public ClassCallExecutor(SourcePosition position, Executor classExecutor, Executor attribute, String name) {
        super(position);
        this.classExecutor = classExecutor;
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("call");
        sb.append(classExecutor.toString());
        sb.append(attribute.toString());
        sb.append(name);

        return sb.toString();
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        String className = ((VariableExecutor) classExecutor).getName();
        BaylObject userClass = virtualMachine.getVariable(className, classExecutor.getPosition());

        if (attribute instanceof FunctionCallExecutor) {
            ((UserClass) userClass).setCall(((FunctionCallExecutor) attribute), virtualMachine);
        } else {
            ((UserClass) userClass).setCall(name, virtualMachine, attribute.getPosition());
        }

        return ((UserClass) userClass).eval(virtualMachine, classExecutor.getPosition());
    }
}
