package org.bayl.runtime.exception;

import org.bayl.SourcePosition;

public class LexerException extends BaylException {
    private static final long serialVersionUID = -6905527358249165699L;

    public LexerException(String message, int lineNo, int column) {
        super(message, new SourcePosition(lineNo, column));
    }
}
