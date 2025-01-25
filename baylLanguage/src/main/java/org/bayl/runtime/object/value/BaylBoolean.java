package org.bayl.runtime.object.value;

import org.bayl.runtime.ValueType;
import org.bayl.runtime.BaylObject;

final public class BaylBoolean extends BaylObject implements ValueType {

    public static final BaylBoolean TRUE = new BaylBoolean(true);
    public static final BaylBoolean FALSE = new BaylBoolean(false);

    private final boolean value;

    private BaylBoolean(boolean value) {
        this.value = value;
    }

    public boolean booleanValue() {
        return this.value;
    }

    static public BaylBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    public BaylBoolean and(BaylBoolean bool) {
        return valueOf(this.value && bool.value);
    }

    public BaylBoolean or(BaylBoolean bool) {
        return valueOf(this.value || bool.value);
    }

    public BaylBoolean not() {
        return valueOf(!this.value);
    }

    @Override
    public BaylString toBaylString() {
        return new BaylString(this.toString());
    }

    @Override
    public BaylObject clone() {
        return new BaylBoolean(value);
    }

    @Override
    public String toString() {
        return this == TRUE ? "true" : "false";
    }

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}
