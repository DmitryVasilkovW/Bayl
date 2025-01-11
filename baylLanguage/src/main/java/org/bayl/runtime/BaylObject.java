package org.bayl.runtime;

import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.SourcePosition;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.object.BaylString;

public abstract class BaylObject implements Comparable<BaylObject> {
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
        if (this instanceof BaylString)
            return (BaylString) this;
        // Implicit converting of types to string
        return new BaylString(this.toString());
    }
}
