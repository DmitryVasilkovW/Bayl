package org.bayl.runtime.exception;

import org.bayl.model.SourcePosition;

public class InvalidTypeException extends BaylException {
    private static final long serialVersionUID = 9115378805326306069L;

    public InvalidTypeException(String message, SourcePosition position) {
        super(message, position);
    }
}
