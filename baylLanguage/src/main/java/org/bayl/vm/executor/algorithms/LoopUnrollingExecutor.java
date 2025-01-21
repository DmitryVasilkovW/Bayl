package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LoopUnrollingExecutor extends BinaryOpExecutor implements ArithmeticOpExecutor {

    private final Executor left;
    private final Executor right;
    private final int iterations;
    private final SourcePosition position;

    public LoopUnrollingExecutor(SourcePosition pos, Executor left, Executor right, int iterations, String operator) {
        // Вызываем конструктор BinaryOpExecutor с нужными параметрами
        super(pos, operator, left, right);

        this.position = pos;
        this.left = left;
        this.right = right;
        this.iterations = iterations;
        this.operator = operator;  // Оператор, который будет использован в вычислениях
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        // Выполняем вычисления для левого и правого операнда
        BaylNumber leftVal = (BaylNumber) left.eval(virtualMachine);
        BaylNumber rightVal = (BaylNumber) right.eval(virtualMachine);

        double result = 0.0;
        int unrolledIterations = iterations / 4 * 4;  // Разворачиваем цикл на 4 итерации за раз

        // Применение Loop Unrolling (разворачивание цикла)
        for (int i = 0; i < unrolledIterations; i += 4) {
            result += applyOperator(leftVal, rightVal);  // 1-й цикл
            result += applyOperator(leftVal, rightVal);  // 2-й цикл
            result += applyOperator(leftVal, rightVal);  // 3-й цикл
            result += applyOperator(leftVal, rightVal);  // 4-й цикл
        }

        // Оставшиеся итерации
        for (int i = unrolledIterations; i < iterations; i++) {
            result += applyOperator(leftVal, rightVal);
        }

        return new BaylNumber(String.valueOf(result));
    }

    // Метод для применения оператора
    private double applyOperator(BaylNumber left, BaylNumber right) {
        switch (operator) {
            case "+":
                return left.intValue() + right.intValue();
            case "-":
                return left.intValue() - right.intValue();
            case "*":
                return left.intValue() * right.intValue();
            case "/":
                return left.intValue() / right.intValue();
            default:
                throw new UnsupportedOperationException("Оператор не поддерживается: " + operator);
        }
    }
}
