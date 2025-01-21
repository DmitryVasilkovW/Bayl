package org.bayl.vm.executor.algorithms;

import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;

import java.util.ArrayList;
import java.util.List;

public class Optimizer {

    public Executor optimizeDCE(Executor executor) {
        if (executor instanceof BlockExecutor) {
            List<Executor> statements = ((BlockExecutor) executor).getStatements();
            List<Executor> optimizedStatements = new ArrayList<>();
            for (Executor statement : statements) {
                Executor optimizedStatement = optimizeDCE(statement);
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
                Executor optimizedArgument = optimizeDCE(argument);
                if (optimizedArgument != null) {
                    optimizedArguments.add(optimizedArgument);
                }
            }
            return new FunctionCallExecutor(executor.getPosition(), functionExecutor, optimizedArguments);
        } else if (executor instanceof FunctionExecutor) {
            List<Executor> parameters = ((FunctionExecutor) executor).getParameters();
            List<Executor> optimizedParameters = new ArrayList<>();
            for (Executor parameter : parameters) {
                Executor optimizedParameter = optimizeDCE(parameter);
                if (optimizedParameter != null) {
                    optimizedParameters.add(optimizedParameter);
                }
            }
            Executor body = ((FunctionExecutor) executor).getBody();
            Executor optimizedBody = optimizeDCE(body);
            return new FunctionExecutor(executor.getPosition(), optimizedParameters, optimizedBody);
        } else if (executor instanceof ReturnExecutor) {
            Executor expression = ((ReturnExecutor) executor).getExpression();
            Executor optimizedExpression = optimizeDCE(expression);
            return new ReturnExecutor(executor.getPosition(), optimizedExpression);
        } else if (executor instanceof VariableExecutor) {
            return executor;
        }
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
        if (executor instanceof FunctionCallExecutor) {
            Executor functionExecutor = ((FunctionCallExecutor) executor).getFunctionExecutor();
            List<Executor> arguments = ((FunctionCallExecutor) executor).getArguments();
            List<Executor> optimizedArguments = new ArrayList<>();
            for (Executor argument : arguments) {
                Executor optimizedArgument = optimizeTailRecursion(argument);
                if (optimizedArgument != null) {
                    optimizedArguments.add(optimizedArgument);
                }
            }
            return new TailRecursionExecutor(executor.getPosition(), functionExecutor, optimizedArguments);
        } else if (executor instanceof FunctionExecutor) {
            List<Executor> parameters = ((FunctionExecutor) executor).getParameters();
            List<Executor> optimizedParameters = new ArrayList<>();
            for (Executor parameter : parameters) {
                Executor optimizedParameter = optimizeTailRecursion(parameter);
                if (optimizedParameter != null) {
                    optimizedParameters.add(optimizedParameter);
                }
            }
            Executor body = ((FunctionExecutor) executor).getBody();
            Executor optimizedBody = optimizeTailRecursion(body);
            return new FunctionExecutor(executor.getPosition(), optimizedParameters, optimizedBody);
        } else if (executor instanceof ReturnExecutor) {
            Executor expression = ((ReturnExecutor) executor).getExpression();
            Executor optimizedExpression = optimizeTailRecursion(expression);
            return new ReturnExecutor(executor.getPosition(), optimizedExpression);
        } else if (executor instanceof VariableExecutor) {
            return executor;
        } else if (executor instanceof BlockExecutor) {
            List<Executor> statements = ((BlockExecutor) executor).getStatements();
            List<Executor> optimizedStatements = new ArrayList<>();
            for (Executor statement : statements) {
                Executor optimizedStatement = optimizeTailRecursion(statement);
                if (optimizedStatement != null) {
                    optimizedStatements.add(optimizedStatement);
                }
            }
            return new BlockExecutor(executor.getPosition(), optimizedStatements);
        }
        return executor;
    }
}