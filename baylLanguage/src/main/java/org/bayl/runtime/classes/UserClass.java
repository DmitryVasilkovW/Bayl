package org.bayl.runtime.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.function.FunctionCallNode;
import org.bayl.ast.expression.function.FunctionNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.ast.statement.AssignNode;
import org.bayl.ast.statement.BlockNode;
import org.bayl.memory.BaylMemory;
import org.bayl.runtime.BaylClass;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.Function;
import org.bayl.runtime.exception.TooFewArgumentsException;
import org.bayl.vm.Environment;

public class UserClass extends BaylClass implements Environment {

    private BaylMemory memory;
    private Map<String, FunctionNode> methods;
    private final BlockNode body;
    private BaylObject call;

    public UserClass(BlockNode body) {
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
            call = methods.get(name).eval(virtualMachine);
            return;
        }
        call = memory.getVariable(name, pos);
    }

    public void setCall(FunctionCallNode functionCallNode, Environment virtualMachine) {
        if (memory == null || methods == null) {
            init(virtualMachine);
        }

        call = functionCallNode.eval(this);
    }

    @Override
    public BaylObject getVariable(String name, SourcePosition pos) {
        try {
            return memory.getVariable(name, pos);
        } catch (Exception e) {
            return methods.get(name).eval(this);
        }
    }

    @Override
    public void setVariable(String name, BaylObject value) {
        memory.setVariable(name, value);
    }

    @Override
    public BaylObject callFunction(Function function, List<BaylObject> args, SourcePosition pos, String functionName) {
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
        for (var node : newHeap.keySet()) {
            if (heap.containsKey(node)) {
                heap.put(node, newHeap.get(node));
            }
        }

        memory.getGlobalStorage().keySet().forEach(
                node -> {
                    if (global.containsKey(node)) {
                        global.put(node, memory.getGlobalStorage().get(node));
                    }
                }
        );

        return ret;
    }

    private void init(Environment virtualMachine) {
        memory = new BaylMemory();
        methods = new HashMap<>();

        body.gerStreamOfStatements().forEach(
                node -> {
                    if (node instanceof AssignNode) {
                        String name = ((VariableNode) ((AssignNode) node).getLeft()).getName();
                        Node value = ((AssignNode) node).getRight();

                        if (value instanceof FunctionNode) {
                            methods.put(name, (FunctionNode) value);
                        } else {
                            memory.setVariable(name, value.eval(virtualMachine));
                        }
                    }
                }
        );
    }
}
