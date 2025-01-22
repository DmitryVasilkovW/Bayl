package org.bayl.runtime.object.value;

import lombok.EqualsAndHashCode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ValueType;

@EqualsAndHashCode(callSuper = false)
public final class BaylNull extends BaylObject implements ValueType {

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }
}
