package org.bayl.vm.executor.algorithms;

import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Optimizer {

    public static Executor optimizeDCE(Executor executor) {
        if (executor instanceof BlockExecutor) {
            List<Executor> statements = ((BlockExecutor) executor).getStatements();
            List<Executor> optimizedStatements = new ArrayList<>();
            Set<String> liveVariables = new HashSet<>();

            // Проходим инструкции в обратном порядке (анализ мёртвых переменных)
            for (int i = statements.size() - 1; i >= 0; i--) {
                Executor statement = statements.get(i);

                if (statement instanceof AssignExecutor) {
                    AssignExecutor assign = (AssignExecutor) statement;
                    Executor left = assign.getLeft();

                    if (left instanceof VariableExecutor) {
                        String assignedVariable = ((VariableExecutor) left).getName();

                        // Если переменная не живая, пропускаем это присваивание
                        if (!liveVariables.contains(assignedVariable)) {
                            continue;
                        }

                        // Добавляем используемые переменные в live set
                        liveVariables.addAll(getUsedVariables(assign.getRight()));
                        optimizedStatements.add(0, assign); // Сохраняем это присваивание
                    }
                } else {
                    // Для других типов инструкций обновляем liveVariables
                    liveVariables.addAll(getUsedVariables(statement));
                    optimizedStatements.add(0, statement);
                }
            }

            // Если блок пустой, возвращаем пустой блок
            if (optimizedStatements.isEmpty()) {
                return new BlockExecutor(executor.getPosition(), List.of());
            }

            return new BlockExecutor(executor.getPosition(), optimizedStatements);
        } else if (executor instanceof ReturnExecutor) {
            // Для ReturnExecutor обновляем liveVariables и оптимизируем выражение
            Executor expression = ((ReturnExecutor) executor).getExpression();
            return new ReturnExecutor(executor.getPosition(), optimizeDCE(expression));
        } else if (executor instanceof AssignExecutor) {
            // Для AssignExecutor оптимизируем правую часть
            AssignExecutor assign = (AssignExecutor) executor;
            return new AssignExecutor(
                    assign.getPosition(),
                    assign.getLeft(),
                    optimizeDCE(assign.getRight())
            );
        } else if (executor instanceof FunctionCallExecutor) {
            // Для вызовов функций оптимизируем все аргументы
            FunctionCallExecutor funcCall = (FunctionCallExecutor) executor;
            List<Executor> optimizedArguments = new ArrayList<>();
            for (Executor arg : funcCall.getArguments()) {
                optimizedArguments.add(optimizeDCE(arg));
            }
            return new FunctionCallExecutor(funcCall.getPosition(), funcCall.getFunctionExecutor(), optimizedArguments);
        }

        // По умолчанию возвращаем сам Executor
        return executor;
    }

    public Executor optimizeLoopUnroll(Executor executor) {
        if (executor instanceof BlockExecutor) {
            List<Executor> statements = ((BlockExecutor) executor).getStatements();
            List<Executor> optimizedStatements = new ArrayList<>();
            for (Executor statement : statements) {
                Executor optimizedStatement = optimizeLoopUnroll(statement);
                if (optimizedStatement != null) {
                    optimizedStatements.add(optimizedStatement);
                }
            }
            return new BlockExecutor(executor.getPosition(), optimizedStatements);
        } else if (executor instanceof FunctionCallExecutor) {
            Executor functionExecutor = ((FunctionCallExecutor) executor).getFunctionExecutor();
            List<Executor> arguments = ((FunctionCallExecutor) executor).getArguments();
            List<Executor> optimizedArguments = new ArrayList<>();
            for (Executor argument : arguments) {
                Executor optimizedArgument = optimizeLoopUnroll(argument);
                if (optimizedArgument != null) {
                    optimizedArguments.add(optimizedArgument);
                }
            }
            return new FunctionCallExecutor(executor.getPosition(), functionExecutor, optimizedArguments);
        } else if (executor instanceof FunctionExecutor) {
            List<Executor> parameters = ((FunctionExecutor) executor).getParameters();
            List<Executor> optimizedParameters = new ArrayList<>();
            for (Executor parameter : parameters) {
                Executor optimizedParameter = optimizeLoopUnroll(parameter);
                if (optimizedParameter != null) {
                    optimizedParameters.add(optimizedParameter);
                }
            }
            Executor body = ((FunctionExecutor) executor).getBody();
            Executor optimizedBody = optimizeLoopUnroll(body);
            return new FunctionExecutor(executor.getPosition(), optimizedParameters, optimizedBody);
        } else if (executor instanceof ReturnExecutor) {
            Executor expression = ((ReturnExecutor) executor).getExpression();
            Executor optimizedExpression = optimizeLoopUnroll(expression);
            return new ReturnExecutor(executor.getPosition(), optimizedExpression);
        } else if (executor instanceof VariableExecutor) {
            return executor;
        }
        return executor;
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

    private static Set<String> getUsedVariables(Executor executor) {
        Set<String> usedVariables = new HashSet<>();
        if (executor instanceof VariableExecutor) {
            usedVariables.add(((VariableExecutor) executor).getName());
        } else if (executor instanceof FunctionCallExecutor) {
            for (Executor arg : ((FunctionCallExecutor) executor).getArguments()) {
                usedVariables.addAll(getUsedVariables(arg));
            }
        } else if (executor instanceof AssignExecutor) {
            usedVariables.addAll(getUsedVariables(((AssignExecutor) executor).getRight()));
        } else if (executor instanceof BlockExecutor) {
            for (Executor statement : ((BlockExecutor) executor).getStatements()) {
                usedVariables.addAll(getUsedVariables(statement));
            }
        } else if (executor instanceof ReturnExecutor) {
            usedVariables.addAll(getUsedVariables(((ReturnExecutor) executor).getExpression()));
        }
        return usedVariables;
    }
}