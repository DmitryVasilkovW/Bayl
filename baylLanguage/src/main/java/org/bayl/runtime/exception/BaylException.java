package org.bayl.runtime.exception;

import org.bayl.SourcePosition;

public class BaylException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private BaylException() {
    }

    public BaylException(String message) {
        super(message);
    }

    public BaylException(String message, SourcePosition position) {
        super(message + " on line " + position.getLineNumber()
                      + " at column " + position.getColumnNumber());
    }
}
