package org.bayl.vm.executor.algorithms;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class DeadCodeEliminationExecutor extends BinaryOpExecutor implements ArithmeticOpExecutor {

    public DeadCodeEliminationExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "DeadCodeElimination", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());

        // Применение Dead Code Elimination: если одно из значений 0, результат также 0.
        if (left.intValue() == 0 || right.intValue() == 0) {
            return new BaylNumber(0);
        }

        // Если оба аргумента действительны, выполняем операцию
        return left.add(right);  // Пример: сложение (вы можете использовать любую операцию)
    }
}
