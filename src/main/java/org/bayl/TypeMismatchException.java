package org.bayl;

import org.bayl.runtime.Dictionary;
import org.bayl.runtime.ZemArray;
import org.bayl.runtime.ZemBoolean;
import org.bayl.runtime.ZemNumber;
import org.bayl.runtime.ZemString;

public class TypeMismatchException extends ZemException {
    private static final long serialVersionUID = 9115378805326306069L;

    static private String toString(Class type) {
        if (type == Dictionary.class) {
            return "dictionary";
        } else if (type == ZemArray.class) {
            return "array";
        } else if (type == ZemBoolean.class) {
            return "boolean";
        } else if (type == ZemNumber.class) {
            return "number";
        } else if (type == ZemString.class) {
            return "string";
        } else {
            return type.getName();
        }
    }

    public TypeMismatchException(SourcePosition pos, Class expect, Class actual) {
        super("Type mismatch - Excepted type '" + toString(expect) + "' but got type '" + toString(actual) + "'", pos);
    }
}
