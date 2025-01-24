package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bayl.bytecode.impl.profiler.Profiler;
import org.bayl.model.BytecodeToken;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;
import org.bayl.vm.impl.VirtualMachineImpl;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
public abstract class BinaryOpExecutor extends Executor {

    protected String operator;
    @Getter
    protected Executor left;
    @Getter
    protected Executor right;

    protected BinaryOpExecutor(SourcePosition pos, String operator, Executor left, Executor right) {
        super(pos);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getName() {
        return operator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(getName());
        sb.append(' ');
        sb.append(left.toString());
        sb.append(' ');
        sb.append(right.toString());
        sb.append(')');
        return sb.toString();
    }

    @Nullable
    protected BytecodeToken getTargetToken() {
        return null;
    }

    protected final NativeParseResult isCanRunNative(BaylObject left, BaylObject right) {
        if (!VirtualMachineImpl.isJitEnabled())
            return new FailedResult();

        var isContainsInstruction = isThresholdViolatorsContainsInstruction();

        if (!isContainsInstruction)
            return new FailedResult();

        var booleanPair = tryParseToBoolean(left, right);
        if (booleanPair != null) {
            return new BooleanPairNativeParseResult(booleanPair.left(), booleanPair.right());
        }

        var doublePair = tryParseToDouble(left, right);
        if (doublePair != null) {
            return new DoublePairNativeParseResult(doublePair.left(), doublePair.right());
        }

        return new FailedResult();
    }

    protected final BaylObjectPair getBaylObjectPair(Environment virtualMachine) {
        BaylObject left = getLeft().eval(virtualMachine);
        BaylObject right = getRight().eval(virtualMachine);
        return new BaylObjectPair(left, right);
    }

    @Nullable
    private Boolean tryParseToBoolean(BaylObject baylObject) {
        if (baylObject instanceof BaylBoolean baylBoolean)
             return baylBoolean.booleanValue();
        else
            return null;
    }

    @Nullable
    private BooleanPair tryParseToBoolean(BaylObject left, BaylObject right) {
        try {
            var leftBoolean = tryParseToBoolean(left);
            var rightBoolean = tryParseToBoolean(right);
            return new BooleanPair(leftBoolean, rightBoolean);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    private Double tryParseToDouble(BaylObject baylObject) {
        BigDecimal value;
        if (baylObject instanceof BaylNumber baylNumber)
            value = baylNumber.getValue();
        else
            return null;

        if (value == null)
            return null;

        double doubleValue = value.doubleValue();
        BigDecimal backToBigDecimal = BigDecimal.valueOf(doubleValue);

        if (value.compareTo(backToBigDecimal) != 0)
            return null;

        return doubleValue;
    }

    @Nullable
    private DoublePair tryParseToDouble(BaylObject left, BaylObject right) {
        try {
            var leftDouble = tryParseToDouble(left);
            var rightDouble = tryParseToDouble(right);

            switch (getTargetToken()) {
                case ADD -> {
                    double result = leftDouble + rightDouble;
                    if (Double.isInfinite(result)) {
                        throw new ArithmeticException();
                    }
                    return new DoublePair(leftDouble, rightDouble);
                }
                case MULTIPLY -> {
                    double result = leftDouble * rightDouble;
                    if (Double.isInfinite(result)) {
                        throw new ArithmeticException();
                    }
                    return new DoublePair(leftDouble, rightDouble);
                }
                case DIVIDE -> {
                    if (rightDouble == 0) {
                        throw new ArithmeticException();
                    }
                    double result = leftDouble / rightDouble;
                    if (Double.isInfinite(result)) {
                        throw new ArithmeticException();
                    }
                    return new DoublePair(leftDouble, rightDouble);
                }
                case SUBTRACT -> {
                    double result = leftDouble - rightDouble;
                    if (Double.isInfinite(result)) {
                        throw new ArithmeticException();
                    }
                    return new DoublePair(leftDouble, rightDouble);
                }
                default -> {
                    return new DoublePair(leftDouble, rightDouble);
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isThresholdViolatorsContainsInstruction() {
        var targetToken = getTargetToken();
        if (targetToken == null)
            return false;

        var profiler = Profiler.getInstance();
        var thresholdViolators = profiler.getThresholdViolators();

        return thresholdViolators.containsKey(getTargetToken());
    }

    protected sealed interface NativeParseResult permits
            BooleanPairNativeParseResult, DoublePairNativeParseResult, FailedResult {}

    protected record BooleanPairNativeParseResult(boolean left, boolean right) implements NativeParseResult {}

    protected record DoublePairNativeParseResult(double left, double right) implements NativeParseResult {}

    protected record FailedResult() implements NativeParseResult {}

    protected record BaylObjectPair(BaylObject left, BaylObject right) {}

    private record BooleanPair(boolean left, boolean right) {}

    private record DoublePair(double left, double right) {}
}
