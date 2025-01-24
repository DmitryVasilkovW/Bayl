package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ForeachExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerTest {

    private final SourcePosition pos = new SourcePosition(0, 0);

    // TAIL RECURSION

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

    @Test
    public void testTailRecursionOptimization_FunctionWithNoReturnStatement() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        AssignExecutor assignment = new AssignExecutor(pos, paramX, new VariableExecutor(pos, "y"));

        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX), new BlockExecutor(pos, List.of(assignment)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        assertEquals(function, optimized); // Functions with no return statements should remain unchanged
    }

    @Test
    public void testTailRecursionOptimization_ComplexRecursiveFunction() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        VariableExecutor paramY = new VariableExecutor(pos, "y");

        AssignExecutor preTailAssignment = new AssignExecutor(pos, paramY, new VariableExecutor(pos, "z"));
        ReturnExecutor tailCall = new ReturnExecutor(pos, new FunctionCallExecutor(pos, paramX, List.of(paramX, paramY)));

        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX, paramY), new BlockExecutor(pos, List.of(
                preTailAssignment, tailCall
        )));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        BlockExecutor expected = new BlockExecutor(pos, List.of(
                preTailAssignment,
                new AssignExecutor(pos, paramX, paramX),
                new AssignExecutor(pos, paramY, paramY),
                new BlockExecutor(pos, List.of())
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testTailRecursionOptimization_LargeRecursionDepth() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");

        // Создаем рекурсивную функцию, которая вызывает себя много раз
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
    public void testTailRecursionOptimization_MultiArgumentFunction() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        VariableExecutor paramY = new VariableExecutor(pos, "y");

        // Пример рекурсивной функции с несколькими параметрами
        ReturnExecutor tailCall = new ReturnExecutor(pos, new FunctionCallExecutor(pos, paramX, List.of(paramX, paramY)));
        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX, paramY), new BlockExecutor(pos, List.of(tailCall)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        BlockExecutor expected = new BlockExecutor(pos, List.of(
                new AssignExecutor(pos, paramX, paramX), // Применение хвостовой оптимизации
                new AssignExecutor(pos, paramY, paramY),
                new BlockExecutor(pos, List.of())
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testTailRecursionOptimization_FunctionWithoutRecursion() {
        VariableExecutor paramX = new VariableExecutor(pos, "x");
        VariableExecutor paramY = new VariableExecutor(pos, "y");

        // Простая функция без рекурсии
        AssignExecutor assignment = new AssignExecutor(pos, paramY, paramX);
        FunctionExecutor function = new FunctionExecutor(pos, List.of(paramX), new BlockExecutor(pos, List.of(assignment)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeTailRecursion(function);

        // Убедитесь, что функция не изменилась
        assertEquals(function, optimized);
    }

    // LOOP UNROLL

    @Test
    public void testForeachUnrolling_SimpleArray() {
        VariableExecutor onVariable = new VariableExecutor(pos, "array");
        VariableExecutor asExecutor = new VariableExecutor(pos, "item");
        AssignExecutor loopBody = new AssignExecutor(pos, asExecutor, onVariable); // Simple assignment inside loop body
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, loopBody);

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeLoopUnrolling(loop, 2);

        // Expected outcome: the loop should unroll twice, duplicating the body twice.
        BlockExecutor expected = new BlockExecutor(pos, List.of(
                new BlockExecutor(pos, List.of(loopBody, loopBody)),  // First unrolled body
                new ForeachExecutor(pos, onVariable, asExecutor, new BlockExecutor(pos, List.of(loopBody)))  // Remaining loop
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testForeachUnrolling_SimpleDictionary() {
        VariableExecutor onVariable = new VariableExecutor(pos, "dict");
        VariableExecutor asExecutor = new VariableExecutor(pos, "entry");
        AssignExecutor loopBody = new AssignExecutor(pos, asExecutor, onVariable); // Simple assignment inside loop body
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, loopBody);

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeLoopUnrolling(loop, 2);

        // Expected outcome: the loop should unroll twice, duplicating the body twice.
        BlockExecutor expected = new BlockExecutor(pos, List.of(
                new BlockExecutor(pos, List.of(loopBody, loopBody)),  // First unrolled body
                new ForeachExecutor(pos, onVariable, asExecutor, new BlockExecutor(pos, List.of(loopBody)))  // Remaining loop
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testForeachUnrolling_NoUnrollWhenFactorOne() {
        VariableExecutor onVariable = new VariableExecutor(pos, "array");
        VariableExecutor asExecutor = new VariableExecutor(pos, "item");
        AssignExecutor loopBody = new AssignExecutor(pos, asExecutor, onVariable); // Simple assignment inside loop body
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, loopBody);

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeLoopUnrolling(loop, 1);

        // Expected outcome: no unrolling should occur when the unroll factor is 1.
        assertEquals(loop, optimized);
    }

    @Test
    public void testForeachUnrolling_ComplexLoopBody() {
        VariableExecutor onVariable = new VariableExecutor(pos, "array");
        VariableExecutor asExecutor = new VariableExecutor(pos, "item");
        AssignExecutor firstAssignment = new AssignExecutor(pos, asExecutor, onVariable);
        AssignExecutor secondAssignment = new AssignExecutor(pos, new VariableExecutor(pos, "temp"), asExecutor);
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, new BlockExecutor(pos, List.of(firstAssignment, secondAssignment)));

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeLoopUnrolling(loop, 3);

        // Expected outcome: the loop body should be unrolled three times.
        BlockExecutor expected = new BlockExecutor(pos, List.of(
                new BlockExecutor(pos, List.of(firstAssignment, secondAssignment, firstAssignment, secondAssignment, firstAssignment, secondAssignment)),  // Unrolled body 3 times
                new ForeachExecutor(pos, onVariable, asExecutor, new BlockExecutor(pos, List.of(firstAssignment, secondAssignment)))  // Remaining loop
        ));

        assertEquals(expected, optimized);
    }

    @Test
    public void testForeachUnrolling_ForeachWithNoBody() {
        VariableExecutor onVariable = new VariableExecutor(pos, "array");
        VariableExecutor asExecutor = new VariableExecutor(pos, "item");
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, null); // Empty body

        Optimizer optimizer = new Optimizer();
        Executor optimized = optimizer.optimizeLoopUnrolling(loop, 2);

        // Expected outcome: no change, as the loop has no body to unroll.
        assertEquals(loop, optimized);
    }
}
