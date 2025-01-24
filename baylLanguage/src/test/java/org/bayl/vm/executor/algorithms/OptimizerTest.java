package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerTest {

    private final SourcePosition pos = new SourcePosition(0, 0);

    @Test
    public void testTailRecursionOptimization_SimpleRecursiveFunction() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        ReturnExecutor tailCall = new ReturnExecutor(pos, new FunctionCallExecutor(pos, paramX, List.of(paramX)));

        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX), new BlockExecutor(pos, List.of(tailCall)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        BlockExecutor expected = new BlockExecutor(pos, List.of(
                new AssignExecutor(pos, paramX, paramX),
                new BlockExecutor(pos, List.of())
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testTailRecursionOptimization_NonTailRecursiveFunction() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        ReturnExecutor returnStatement = new ReturnExecutor(pos, paramX);

        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX), new BlockExecutor(pos, List.of(returnStatement)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        assertEquals(function, optimized); // Non-tail-recursive functions should remain unchanged
    }

    @Test
    public void testTailRecursionOptimization_FunctionWithMultipleStatements() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        VariableExecutor paramY = new VariableExecutor(pos, "y");

        AssignExecutor assignment = new AssignExecutor(pos, paramY, paramX);
        ReturnExecutor tailCall = new ReturnExecutor(pos, new FunctionCallExecutor(pos, paramX, List.of(paramY)));

        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX, paramY), new BlockExecutor(pos, List.of(
                assignment, tailCall
        )));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        BlockExecutor expected = new BlockExecutor(pos, List.of(
                assignment,
                new AssignExecutor(pos, paramX, paramY),
                new BlockExecutor(pos, List.of())
        ));

        assertEquals(expected, optimized);
    }
}
