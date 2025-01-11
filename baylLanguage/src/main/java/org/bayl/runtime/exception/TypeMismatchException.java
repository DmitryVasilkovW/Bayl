package org.bayl.runtime.exception;

import org.bayl.SourcePosition;
import org.bayl.runtime.object.Dictionary;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.object.BaylString;

public class TypeMismatchException extends BaylException {
    private static final long serialVersionUID = 9115378805326306069L;

    static private String toString(Class type) {
        if (type == Dictionary.class) {
            return "dictionary";
        } else if (type == BaylArray.class) {
            return "array";
        } else if (type == BaylBoolean.class) {
            return "boolean";
        } else if (type == BaylNumber.class) {
            return "number";
        } else if (type == BaylString.class) {
            return "string";
        } else {
            return type.getName();
        }
    }

    public TypeMismatchException(SourcePosition pos, Class expect, Class actual) {
        super("Type mismatch - Excepted type '" + toString(expect) + "' but got type '" + toString(actual) + "'", pos);
    }
}
