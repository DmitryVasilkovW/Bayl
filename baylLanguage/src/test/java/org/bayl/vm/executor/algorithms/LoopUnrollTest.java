package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ForeachExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoopUnrollTest {
    private final SourcePosition pos = new SourcePosition(0, 0);

    @Test
    public void testForeachUnrolling_SimpleArray() {
        VariableExecutor onVariable = new VariableExecutor(pos, "array");
        VariableExecutor asExecutor = new VariableExecutor(pos, "item");
        AssignExecutor loopBody = new AssignExecutor(pos, asExecutor, onVariable); // Simple assignment inside loop body
        ForeachExecutor loop = new ForeachExecutor(pos, onVariable, asExecutor, loopBody);

        Executor optimized = new LoopUnroll().optimizeLoopUnrolling(loop, 2);

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

        Executor optimized = new LoopUnroll().optimizeLoopUnrolling(loop, 2);

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

        Executor optimized = new LoopUnroll().optimizeLoopUnrolling(loop, 1);

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

        Executor optimized = new LoopUnroll().optimizeLoopUnrolling(loop, 3);

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

        Executor optimized = new LoopUnroll().optimizeLoopUnrolling(loop, 2);

        // Expected outcome: no change, as the loop has no body to unroll.
        assertEquals(loop, optimized);
    }
}
