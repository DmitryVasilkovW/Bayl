package org.bayl.runtime.object.value;

import lombok.Getter;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ValueType;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
final public class BaylNumber extends BaylObject implements ValueType {

    private final BigDecimal value;

    public BaylNumber(String value) {
        String prefix = "";
        if (value.length() > 2) {
            prefix = value.substring(0, 2).toLowerCase();
        }
        switch (prefix) {
            case "0b" -> this.value = new BigDecimal(new BigInteger(value.substring(2), 2));
            case "0o" -> this.value = new BigDecimal(new BigInteger(value.substring(2), 8));
            case "0x" -> this.value = new BigDecimal(new BigInteger(value.substring(2), 16));
            default -> this.value = new BigDecimal(value);
        }
    }

    public BaylNumber(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public BaylNumber(BigDecimal value) {
        this.value = value;
    }

    public BaylNumber(int value) {
        this.value = new BigDecimal(value);
    }

    public BaylNumber add(BaylNumber augend) {
        return new BaylNumber(value.add(augend.value));
    }

    public BaylNumber subtract(BaylNumber subtrahend) {
        return new BaylNumber(value.subtract(subtrahend.value));
    }

    public BaylNumber multiply(BaylNumber multiplicand) {
        return new BaylNumber(value.multiply(multiplicand.value));
    }

    public BaylNumber divide(BaylNumber divisor) {
        return new BaylNumber(value.divide(divisor.value));
    }

    public BaylNumber remainder(BaylNumber divisor) {
        return new BaylNumber(value.remainder(divisor.value));
    }

    public BaylNumber power(BaylNumber n) {
        return new BaylNumber(value.pow(n.value.intValueExact()));
    }

    public BaylNumber negate() {
        return new BaylNumber(value.negate());
    }

    public BaylNumber max(BaylNumber num) {
        return new BaylNumber(value.max(num.value));
    }

    public BaylNumber min(BaylNumber num) {
        return new BaylNumber(value.min(num.value));
    }

    public int intValue() {
        return value.intValue();
    }

    public int compareTo(BaylObject object) {
        BaylNumber number = (BaylNumber) object;
        return value.compareTo(number.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object object) {
        return compareTo((BaylObject) object) == 0;
    }

    @Override
    public BaylObject clone() {
        return new BaylNumber(value);
    }
}
