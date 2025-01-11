package org.bayl.runtime.object;

import org.bayl.runtime.BaylObject;

final public class BaylBoolean extends BaylObject {
    static final public BaylBoolean TRUE = new BaylBoolean(true);
    static final public BaylBoolean FALSE = new BaylBoolean(false);

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
}
