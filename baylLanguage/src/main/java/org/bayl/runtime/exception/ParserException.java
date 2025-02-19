package org.bayl.runtime.exception;

import org.bayl.model.SourcePosition;

public class ParserException extends BaylException {
    private static final long serialVersionUID = 7505060960165209530L;

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, SourcePosition position) {
        super(message, position);
    }
}
