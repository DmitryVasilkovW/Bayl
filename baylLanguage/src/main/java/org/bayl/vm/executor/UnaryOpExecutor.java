package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bayl.bytecode.impl.profiler.Profiler;
import org.bayl.model.BytecodeToken;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.impl.VirtualMachineImpl;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
public abstract class UnaryOpExecutor extends Executor {

    protected String operator;
    @Getter
    protected Executor operand;

    public UnaryOpExecutor(SourcePosition pos, String operator, Executor operand) {
        super(pos);
        this.operator = operator;
        this.operand = operand;
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
        sb.append(operand.toString());
        sb.append(')');
        return sb.toString();
    }

    @Nullable
    protected BytecodeToken getTargetToken() {
        return null;
    }

    protected final NativeParseResult isCanRunNative(BaylObject operand) {
        if (!VirtualMachineImpl.isJitEnabled())
            return new FailedResult();

        var isContainsInstruction = isThresholdViolatorsContainsInstruction();

        if (!isContainsInstruction)
            return new FailedResult();

        var booleanNativeRunResult = tryParseToBoolean(operand);
        if (booleanNativeRunResult != null) {
            return new BooleanNativeParseResult(booleanNativeRunResult);
        }

        var doubleNativeRunResult = tryParseToDouble(operand);
        if (doubleNativeRunResult != null) {
            return new DoubleNativeParseResult(doubleNativeRunResult);
        }

        return new FailedResult();
    }

    private boolean isThresholdViolatorsContainsInstruction() {
        var targetToken = getTargetToken();
        if (targetToken == null)
            return false;

        var profiler = Profiler.getInstance();
        var thresholdViolators = profiler.getThresholdViolators();

        return thresholdViolators.containsKey(getTargetToken());
    }

    @Nullable
    private Boolean tryParseToBoolean(BaylObject baylObject) {
        if (baylObject instanceof BaylBoolean baylBoolean)
            return baylBoolean.booleanValue();
        else
            return null;
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

    protected sealed interface NativeParseResult permits
            BooleanNativeParseResult, DoubleNativeParseResult, FailedResult {}

    protected record FailedResult() implements NativeParseResult {}

    protected record BooleanNativeParseResult(boolean left) implements NativeParseResult {}

    protected record DoubleNativeParseResult(double left) implements NativeParseResult {}
}
