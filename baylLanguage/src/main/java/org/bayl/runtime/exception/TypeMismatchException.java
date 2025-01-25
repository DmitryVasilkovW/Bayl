package org.bayl.runtime.exception;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.object.ref.BaylDictionary;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.runtime.object.value.BaylString;

public class TypeMismatchException extends BaylException {
    private static final long serialVersionUID = 9115378805326306069L;

    static private String toString(Class type) {
        if (type == BaylDictionary.class) {
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
