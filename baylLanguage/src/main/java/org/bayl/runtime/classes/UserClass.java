package org.bayl.runtime.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.bayl.model.SourcePosition;
import org.bayl.memory.BaylMemory;
import org.bayl.runtime.BaylClass;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.exception.TooFewArgumentsException;
import org.bayl.runtime.function.UserFunction;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;

@AllArgsConstructor
public class UserClass extends BaylClass implements Environment {

    private BaylMemory memory;
    private Map<String, BaylFunction> methods;
    private final BlockExecutor body;
    private BaylObject call;

    public UserClass(BlockExecutor body) {
        this.body = body;
    }

    @Override
    public BaylObject eval(Environment virtualMachine, SourcePosition pos) {
        return call;
    }

    public void setCall(String name, Environment virtualMachine, SourcePosition pos) {
        if (memory == null || methods == null) {
            init(virtualMachine);
        }

        if (methods.containsKey(name)) {
            call = methods.get(name);
            return;
        }
        call = memory.getVariable(name, pos);
    }

    public void setCall(FunctionCallExecutor functionCallExecutor, Environment virtualMachine) {
        if (memory == null || methods == null) {
            init(virtualMachine);
        }

        call = functionCallExecutor.eval(this);
    }

    @Override
    public BaylObject getVariable(String name, SourcePosition pos) {
        try {
            return memory.getVariable(name, pos);
        } catch (Exception e) {
            return methods.get(name);
        }
    }

    @Override
    public void setVariable(String name, BaylObject value) {
        memory.setVariable(name, value);
    }

    @Override
    public BaylObject callFunction(BaylFunction function, List<BaylObject> args, SourcePosition pos, String functionName) {
        var heap = new HashMap<>(memory.getHeap());
        var global = new HashMap<>(memory.getGlobalStorage());

        int noMissingArgs = 0;
        int noRequiredArgs = 0;
        for (int paramIndex = 0;
             paramIndex < function.getParameterCount(); paramIndex++) {
            String parameterName = function.getParameterName(paramIndex);
            BaylObject value = function.getDefaultValue(paramIndex);
            if (value == null) {
                noRequiredArgs++;
            }
            if (paramIndex < args.size()) {
                value = args.get(paramIndex);
            }
            if (value == null) {
                noMissingArgs++;
            }
            setVariable(parameterName, value);
        }
        if (noMissingArgs > 0) {
            throw new TooFewArgumentsException(functionName, noRequiredArgs,
                                               args.size(), pos);
        }

        var ret = function.eval(this, pos);

        var newHeap = memory.getHeap();
        for (var Executor : newHeap.keySet()) {
            if (heap.containsKey(Executor)) {
                heap.put(Executor, newHeap.get(Executor));
            }
        }

        memory.getGlobalStorage().keySet().forEach(
                Executor -> {
                    if (global.containsKey(Executor)) {
                        global.put(Executor, memory.getGlobalStorage().get(Executor));
                    }
                }
        );

        return ret;
    }

    private void init(Environment virtualMachine) {
        memory = new BaylMemory();
        methods = new HashMap<>();

        body.gerStreamOfStatements().forEach(
                executor -> {
                    if (executor instanceof AssignExecutor) {
                        String name = ((VariableExecutor) ((AssignExecutor) executor).getLeft()).getName();
                        Executor value = ((AssignExecutor) executor).getRight();

                        if (value instanceof FunctionExecutor) {
                            methods.put(name, ((UserFunction) (value).eval(virtualMachine)));
                        } else {
                            memory.setVariable(name, value.eval(virtualMachine));
                        }
                    }
                }
        );
    }

    @Override
    public BaylObject clone() {
        BaylObject callClone = null;
        if (call != null) {
            callClone = call.clone();
        }

        return new UserClass(memory.clone(), methods, body, callClone);
    }
}
