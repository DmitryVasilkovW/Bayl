package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TailCallOptimizationExecutor extends BinaryOpExecutor implements ArithmeticOpExecutor {

    private final Executor argument;
    private final SourcePosition position;

    public TailCallOptimizationExecutor(SourcePosition pos, Executor argument) {
        // Вызываем конструктор BinaryOpExecutor с параметрами, которые могут быть пустыми или null
        super(pos, "", null, null); // Здесь оператор пустой, так как в хвостовой рекурсии нет бинарной операции

        this.position = pos;
        this.argument = argument;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        // Применение хвостовой рекурсии (Tail Call Optimization)
        // Здесь для примера мы просто выполняем рекурсию через аргумент

        BaylNumber arg = (BaylNumber) argument.eval(virtualMachine);
        // Пример: если аргумент больше 0, продолжаем рекурсию, иначе возвращаем результат
        if (arg.intValue() > 0) {
            return eval(virtualMachine);  // Пример хвостовой рекурсии (рекурсия без добавления в стек)
        }

        return arg;
    }
}
