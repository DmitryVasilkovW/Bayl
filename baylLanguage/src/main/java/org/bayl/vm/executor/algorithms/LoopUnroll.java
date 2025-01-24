package org.bayl.vm.executor.algorithms;

import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.ForeachExecutor;
import org.bayl.vm.executor.statement.IfExecutor;
import org.bayl.vm.executor.statement.WhileExecutor;

import java.util.ArrayList;
import java.util.List;

public class LoopUnroll {

    public Executor optimizeLoopUnrolling(Executor executor, int unrollFactor) {
        if (unrollFactor == 1) {
            return executor;
        }

        if (executor instanceof WhileExecutor) {
            return optimizeWhileLoop((WhileExecutor) executor, unrollFactor);
        } else if (executor instanceof ForeachExecutor) {
            return optimizeForeachLoop((ForeachExecutor) executor, unrollFactor);
        }
        return executor;
    }

    private Executor optimizeWhileLoop(WhileExecutor loop, int unrollFactor) {
        Executor condition = loop.getTestCondition();
        BlockExecutor loopBody = getLoopBodyAsBlock(loop.getLoopBody(), loop);

        List<Executor> unrolledStatements = generateUnrolledStatements(loopBody.getStatements(), unrollFactor);
        WhileExecutor remainingLoop = createRemainingWhileLoop(loop, condition, loopBody);

        return createUnrolledWhileBlock(loop, condition, unrolledStatements, remainingLoop);
    }

    private BlockExecutor getLoopBodyAsBlock(Executor body, WhileExecutor loop) {
        if (!(body instanceof BlockExecutor)) {
            return new BlockExecutor(loop.getPosition(), List.of(body));
        }
        return (BlockExecutor) body;
    }

    private List<Executor> generateUnrolledStatements(List<Executor> originalStatements, int unrollFactor) {
        List<Executor> unrolledStatements = new ArrayList<>();
        for (int i = 0; i < unrollFactor; i++) {
            unrolledStatements.addAll(originalStatements);
        }
        return unrolledStatements;
    }

    private WhileExecutor createRemainingWhileLoop(WhileExecutor loop, Executor condition, BlockExecutor loopBody) {
        return new WhileExecutor(
                loop.getPosition(),
                condition,
                new BlockExecutor(loop.getPosition(), loopBody.getStatements())
        );
    }

    private BlockExecutor createUnrolledWhileBlock(WhileExecutor loop, Executor condition, List<Executor> unrolledStatements, WhileExecutor remainingLoop) {
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

    private Executor optimizeForeachLoop(ForeachExecutor loop, int unrollFactor) {
        if (loop.getLoopBody() == null) {
            return loop;
        }

        VariableExecutor onVariableExecutor = loop.getOnVariableExecutor();
        Executor asExecutor = loop.getAsExecutor();
        BlockExecutor loopBody = getLoopBodyAsBlock(loop.getLoopBody(), loop);

        if (isArrayLoop(onVariableExecutor)) {
            return optimizeArrayForeachLoop(loop, unrollFactor, onVariableExecutor, asExecutor, loopBody);
        } else {
            return optimizeDictionaryForeachLoop(loop, unrollFactor, onVariableExecutor, asExecutor, loopBody);
        }
    }

    private boolean isArrayLoop(VariableExecutor onVariableExecutor) {
        return onVariableExecutor.toString().contains("BaylArray");
    }

    private BlockExecutor getLoopBodyAsBlock(Executor body, ForeachExecutor loop) {
        if (!(body instanceof BlockExecutor)) {
            return new BlockExecutor(loop.getPosition(), List.of(body));
        }
        return (BlockExecutor) body;
    }

    private Executor optimizeArrayForeachLoop(
            ForeachExecutor loop,
            int unrollFactor,
            VariableExecutor onVariableExecutor,
            Executor asExecutor,
            BlockExecutor loopBody) {

        List<Executor> unrolledStatements = generateUnrolledStatements(loopBody.getStatements(), unrollFactor);
        ForeachExecutor remainingLoop = createRemainingForeachLoop(loop, onVariableExecutor, asExecutor, loopBody);

        return createUnrolledForeachBlock(loop, unrolledStatements, remainingLoop);
    }

    private ForeachExecutor createRemainingForeachLoop(ForeachExecutor loop, VariableExecutor onVariableExecutor, Executor asExecutor, BlockExecutor loopBody) {
        return new ForeachExecutor(
                loop.getPosition(),
                onVariableExecutor,
                asExecutor,
                new BlockExecutor(loop.getPosition(), loopBody.getStatements())
        );
    }

    private BlockExecutor createUnrolledForeachBlock(ForeachExecutor loop, List<Executor> unrolledStatements, ForeachExecutor remainingLoop) {
        return new BlockExecutor(
                loop.getPosition(),
                List.of(
                        new BlockExecutor(loop.getPosition(), unrolledStatements),
                        remainingLoop
                )
        );
    }

    private Executor optimizeDictionaryForeachLoop(
            ForeachExecutor loop,
            int unrollFactor,
            VariableExecutor onVariableExecutor,
            Executor asExecutor,
            BlockExecutor loopBody) {

        List<Executor> unrolledStatements = generateUnrolledStatements(loopBody.getStatements(), unrollFactor);
        ForeachExecutor remainingLoop = createRemainingForeachLoop(loop, onVariableExecutor, asExecutor, loopBody);

        return createUnrolledForeachBlock(loop, unrolledStatements, remainingLoop);
    }
}
