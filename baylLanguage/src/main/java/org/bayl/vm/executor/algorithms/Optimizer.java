package org.bayl.vm.executor.algorithms;

import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ForeachExecutor;
import org.bayl.vm.executor.statement.IfExecutor;
import org.bayl.vm.executor.statement.WhileExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Optimizer {
    public Executor optimizeLoopUnrolling(Executor executor, int unrollFactor) {
        if (unrollFactor == 1) {
            return executor;
        }

        if (executor instanceof WhileExecutor) {
            return unrollWhileLoop((WhileExecutor) executor, unrollFactor);
        } else if (executor instanceof ForeachExecutor) {
            return unrollForeachLoop((ForeachExecutor) executor, unrollFactor);
        }
        return executor;
    }

    private Executor unrollWhileLoop(WhileExecutor loop, int unrollFactor) {
        Executor condition = loop.getTestCondition();
        Executor body = loop.getLoopBody();

        if (!(body instanceof BlockExecutor)) {
            body = new BlockExecutor(loop.getPosition(), List.of(body));
        }

        List<Executor> originalStatements = ((BlockExecutor) body).getStatements();
        List<Executor> unrolledStatements = new ArrayList<>();

        // Разворачиваем тело цикла
        for (int i = 0; i < unrollFactor; i++) {
            unrolledStatements.addAll(originalStatements);
        }

        // Создаем новый цикл с оставшимися итерациями
        WhileExecutor remainingLoop = new WhileExecutor(
                loop.getPosition(),
                condition,
                new BlockExecutor(loop.getPosition(), originalStatements)
        );

        // Создаем блок с развёрнутым телом и оставшимся циклом
        return new BlockExecutor(
                loop.getPosition(),
                List.of(
                        new IfExecutor(
                                loop.getPosition(),
                                condition,
                                new BlockExecutor(loop.getPosition(), unrolledStatements),
                                remainingLoop
                        )
                )
        );
    }

    public Executor unrollForeachLoop(ForeachExecutor loop, int unrollFactor) {
        if (loop.getLoopBody() == null) {
            return loop;
        }

        VariableExecutor onVariableExecutor = loop.getOnVariableExecutor();
        Executor asExecutor = loop.getAsExecutor();
        Executor loopBody = loop.getLoopBody();

        if (!(loopBody instanceof BlockExecutor)) {
            loopBody = new BlockExecutor(loop.getPosition(), List.of(loopBody));
        }

        // Поддержка BaylArray и Dictionary
        if (loop.getOnVariableExecutor().toString().contains("BaylArray")) {
            return optimizeArrayForeach(loop, unrollFactor, onVariableExecutor, asExecutor, (BlockExecutor) loopBody);
        } else {
            return optimizeDictionaryForeach(loop, unrollFactor, onVariableExecutor, asExecutor, (BlockExecutor) loopBody);
        }
    }

    private Executor optimizeArrayForeach(
            ForeachExecutor loop,
            int unrollFactor,
            VariableExecutor onVariableExecutor,
            Executor asExecutor,
            BlockExecutor loopBody) {

        List<Executor> unrolledStatements = new ArrayList<>();
        for (int i = 0; i < unrollFactor; i++) {
            unrolledStatements.addAll(loopBody.getStatements());
        }

        ForeachExecutor remainingLoop = new ForeachExecutor(
                loop.getPosition(),
                onVariableExecutor,
                asExecutor,
                new BlockExecutor(loop.getPosition(), loopBody.getStatements())
        );

        return new BlockExecutor(
                loop.getPosition(),
                List.of(
                        new BlockExecutor(loop.getPosition(), unrolledStatements),
                        remainingLoop
                )
        );
    }

    private Executor optimizeDictionaryForeach(
            ForeachExecutor loop,
            int unrollFactor,
            VariableExecutor onVariableExecutor,
            Executor asExecutor,
            BlockExecutor loopBody) {

        List<Executor> unrolledStatements = new ArrayList<>();
        for (int i = 0; i < unrollFactor; i++) {
            unrolledStatements.addAll(loopBody.getStatements());
        }

        ForeachExecutor remainingLoop = new ForeachExecutor(
                loop.getPosition(),
                onVariableExecutor,
                asExecutor,
                new BlockExecutor(loop.getPosition(), loopBody.getStatements())
        );

        return new BlockExecutor(
                loop.getPosition(),
                List.of(
                        new BlockExecutor(loop.getPosition(), unrolledStatements),
                        remainingLoop
                )
        );
    }

    public Executor optimizeTailRecursion(Executor executor) {
        if (executor instanceof FunctionExecutor) {
            return optimizeFunctionTailRecursion((FunctionExecutor) executor);
        } else if (executor instanceof BlockExecutor) {
            List<Executor> optimizedStatements = new ArrayList<>();
            for (Executor statement : ((BlockExecutor) executor).getStatements()) {
                optimizedStatements.add(optimizeTailRecursion(statement));
            }
            return new BlockExecutor(executor.getPosition(), optimizedStatements);
        }
        return executor;
    }

    private Executor optimizeFunctionTailRecursion(FunctionExecutor function) {
        Executor body = function.getBody();

        if (body instanceof BlockExecutor) {
            List<Executor> statements = ((BlockExecutor) body).getStatements();
            List<Executor> newStatements = new ArrayList<>();

            boolean hasTailCall = false;

            for (int i = 0; i < statements.size(); i++) {
                Executor statement = statements.get(i);

                // Если это return с вызовом текущей функции, преобразуем его
                if (statement instanceof ReturnExecutor returnExecutor) {
                    Executor expression = returnExecutor.getExpression();
                    if (expression instanceof FunctionCallExecutor functionCall &&
                            isTailCall(function, functionCall)) {

                        hasTailCall = true;
                        List<Executor> args = functionCall.getArguments();

                        // Преобразуем аргументы в присваивания
                        for (int j = 0; j < args.size(); j++) {
                            newStatements.add(new AssignExecutor(
                                    functionCall.getPosition(),
                                    function.getParameters().get(j),
                                    args.get(j)
                            ));
                        }

                        // Добавляем переход к началу функции
                        newStatements.add(new BlockExecutor(functionCall.getPosition(), List.of()));
                        continue;
                    }
                }

                newStatements.add(statement);
            }

            if (hasTailCall) {
                // Оборачиваем цикл в метку
                return new BlockExecutor(function.getPosition(), newStatements);
            }
        }

        return function;
    }

    private boolean isTailCall(FunctionExecutor function, FunctionCallExecutor call) {
        return call.getFunctionExecutor() instanceof VariableExecutor variable &&
                function.getParameters().contains(variable);
    }
}