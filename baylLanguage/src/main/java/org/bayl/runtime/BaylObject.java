package org.bayl.runtime;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.runtime.object.value.BaylString;

public abstract class BaylObject implements BaylType, Comparable<BaylObject>, Cloneable {

    public BaylNumber toNumber(SourcePosition pos) {
        if (this instanceof BaylNumber) {
            return (BaylNumber) this;
        }
        throw new InvalidTypeException("Expecting number", pos);
    }

    public BaylBoolean toBoolean(SourcePosition pos) {
        if (this instanceof BaylBoolean) {
            return (BaylBoolean) this;
        }
        throw new InvalidTypeException("Expecting boolean", pos);
    }

    public BaylString toBaylString() {
        if (this instanceof BaylString) {
            return (BaylString) this;
        }
        return new BaylString(this.toString());
    }

    @Override
    public abstract BaylObject clone();
}
