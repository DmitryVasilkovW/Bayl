package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerTest {
    private SourcePosition pos = new SourcePosition(0,0);

    @Test
    public void DCE_DoesRemoveUnusedExpressions() {
        List<Executor> statements = List.of(
                new AssignExecutor(pos, new VariableExecutor(pos, "x"), new NumberExecutor(pos, "3")),
                new AssignExecutor(pos, new VariableExecutor(pos, "y"), new VariableExecutor(pos, "x")),
                new AssignExecutor(pos, new VariableExecutor(pos, "z"), new VariableExecutor(pos, "0"))
        );

        Executor executor = new BlockExecutor(
                pos,
                statements
        );

        Executor result = Optimizer.optimizeDCE(executor);
        Executor expected = new BlockExecutor(pos, List.of(
                new AssignExecutor(pos, new VariableExecutor(pos, "x"), new NumberExecutor(pos, "3")),
                new AssignExecutor(pos, new VariableExecutor(pos, "y"), new VariableExecutor(pos, "x"))
        ));

        assertEquals(result, expected);
    }

    @Test
    public void DCE_DoesNotRemoveUsedExpressions() {
        List<Executor> statements = List.of(
                new AssignExecutor(pos, new VariableExecutor(pos, "x"), new NumberExecutor(pos, "3")),
                new AssignExecutor(pos, new VariableExecutor(pos, "y"), new VariableExecutor(pos, "x")),
                new AssignExecutor(pos, new VariableExecutor(pos, "z"), new VariableExecutor(pos, "y"))
        );

        Executor executor = new BlockExecutor(
                pos,
                statements
        );

        Executor result = Optimizer.optimizeDCE(executor);
        Executor expected = new BlockExecutor(pos, statements);

        assertEquals(result, expected);
    }


}
