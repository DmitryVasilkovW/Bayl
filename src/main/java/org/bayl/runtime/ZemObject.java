package org.bayl.runtime;

import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.SourcePosition;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.object.ZemNumber;
import org.bayl.runtime.object.ZemString;

public abstract class ZemObject implements Comparable<ZemObject> {
    public ZemNumber toNumber(SourcePosition pos) {
        if (this instanceof ZemNumber) {
            return (ZemNumber) this;
        }
        throw new InvalidTypeException("Expecting number", pos);
    }

    public ZemBoolean toBoolean(SourcePosition pos) {
        if (this instanceof ZemBoolean) {
            return (ZemBoolean) this;
        }
        throw new InvalidTypeException("Expecting boolean", pos);
    }

    public ZemString toZString() {
    /*
        if (this instanceof ZemString) {
            return (ZemString) this;
        }
        throw new InvalidTypeException("Expecting string");
     */
        if (this instanceof ZemString)
            return (ZemString) this;
        // Implicit converting of types to string
        return new ZemString(this.toString());
    }
}
