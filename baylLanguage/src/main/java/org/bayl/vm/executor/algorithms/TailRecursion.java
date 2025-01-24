package org.bayl.vm.executor.algorithms;

import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;

import java.util.ArrayList;
import java.util.List;

public class TailRecursion {

    public Executor optimizeTailRecursion(Executor executor) {
        if (executor instanceof FunctionExecutor functionExecutor) {
            return optimizeFunctionTailRecursion(functionExecutor);
        } else if (executor instanceof BlockExecutor blockExecutor) {
            return optimizeBlockTailRecursion(blockExecutor);
        }
        return executor;
    }

    private Executor optimizeBlockTailRecursion(BlockExecutor blockExecutor) {
        List<Executor> optimizedStatements = new ArrayList<>();
        for (Executor statement : blockExecutor.getStatements()) {
            optimizedStatements.add(optimizeTailRecursion(statement));
        }
        return new BlockExecutor(blockExecutor.getPosition(), optimizedStatements);
    }

    private Executor optimizeFunctionTailRecursion(FunctionExecutor function) {
        Executor body = function.getBody();

        if (body instanceof BlockExecutor blockExecutor) {
            return processFunctionBody(blockExecutor, function);
        }

        return function;
    }

    private Executor processFunctionBody(BlockExecutor blockExecutor, FunctionExecutor function) {
        List<Executor> statements = blockExecutor.getStatements();
        List<Executor> newStatements = new ArrayList<>();

        boolean hasTailCall = processStatements(statements, newStatements, function);

        return hasTailCall ? wrapInLoop(newStatements, function) : function;
    }

    private boolean processStatements(List<Executor> statements, List<Executor> newStatements, FunctionExecutor function) {
        boolean hasTailCall = false;

        for (Executor statement : statements) {
            if (statement instanceof ReturnExecutor returnExecutor) {
                if (processReturnStatement(returnExecutor, newStatements, function)) {
                    hasTailCall = true;
                    continue;
                }
            }
            newStatements.add(statement);
        }

        return hasTailCall;
    }

    private boolean processReturnStatement(ReturnExecutor returnExecutor, List<Executor> newStatements, FunctionExecutor function) {
        Executor expression = returnExecutor.getExpression();

        if (expression instanceof FunctionCallExecutor functionCall && isTailCall(function, functionCall)) {
            transformTailCall(newStatements, function, functionCall);
            return true;
        }

        return false;
    }

    private void transformTailCall(List<Executor> newStatements, FunctionExecutor function, FunctionCallExecutor functionCall) {
        List<Executor> args = functionCall.getArguments();

        for (int i = 0; i < args.size(); i++) {
            newStatements.add(createAssignStatement(function, functionCall, i));
        }

        addLoopJump(newStatements, functionCall);
    }

    private AssignExecutor createAssignStatement(FunctionExecutor function, FunctionCallExecutor functionCall, int index) {
        return new AssignExecutor(
                functionCall.getPosition(),
                function.getParameters().get(index),
                functionCall.getArguments().get(index)
        );
    }

    private void addLoopJump(List<Executor> newStatements, FunctionCallExecutor functionCall) {
        newStatements.add(new BlockExecutor(functionCall.getPosition(), List.of()));
    }

    private Executor wrapInLoop(List<Executor> newStatements, FunctionExecutor function) {
        return new BlockExecutor(function.getPosition(), newStatements);
    }

    private boolean isTailCall(FunctionExecutor function, FunctionCallExecutor call) {
        return call.getFunctionExecutor() instanceof VariableExecutor variable &&
                function.getParameters().contains(variable);
    }
}
