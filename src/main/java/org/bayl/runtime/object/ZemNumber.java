package org.bayl.runtime.object;

import org.bayl.runtime.ZemObject;
import java.math.BigInteger;
import java.math.BigDecimal;

final public class ZemNumber extends ZemObject {
    private final BigDecimal value;

    public ZemNumber(String value) {
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

    ZemNumber(BigDecimal value) {
        this.value = value;
    }

    public ZemNumber(int value) {
        this.value = new BigDecimal(value);
    }

    public ZemNumber add(ZemNumber augend) {
        return new ZemNumber(value.add(augend.value));
    }

    public ZemNumber subtract(ZemNumber subtrahend) {
        return new ZemNumber(value.subtract(subtrahend.value));
    }

    public ZemNumber multiply(ZemNumber multiplicand) {
        return new ZemNumber(value.multiply(multiplicand.value));
    }

    public ZemNumber divide(ZemNumber divisor) {
        return new ZemNumber(value.divide(divisor.value));
    }

    public ZemNumber remainder(ZemNumber divisor) {
        return new ZemNumber(value.remainder(divisor.value));
    }

    public ZemNumber power(ZemNumber n) {
        return new ZemNumber(value.pow(n.value.intValueExact()));
    }

    public ZemNumber negate() {
        return new ZemNumber(value.negate());
    }

    public int intValue() {
        return value.intValue();
    }

    public int compareTo(ZemObject object) {
        ZemNumber number = (ZemNumber) object;
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
        return compareTo((ZemObject) object) == 0;
    }
}
