package org.bayl.runtime.object;

import org.bayl.runtime.BaylMeaningful;
import org.bayl.runtime.BaylObject;

final public class BaylString extends BaylObject implements BaylMeaningful {

    private final String value;

    public BaylString(String value) {
        this.value = value;
    }

    public BaylString concat(BaylString other) {
        return new BaylString(value + other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(BaylObject object) {
        BaylString str = (BaylString) object;
        return value.compareTo(str.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return compareTo((BaylObject) object) == 0;
    }
}
