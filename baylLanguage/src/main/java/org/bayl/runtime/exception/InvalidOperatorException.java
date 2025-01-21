package org.bayl.runtime.exception;

import org.bayl.model.SourcePosition;

public class InvalidOperatorException extends BaylException {
    private static final long serialVersionUID = -57261291654807212L;

    public InvalidOperatorException(SourcePosition pos) {
        super("Invalid operator", pos);
    }
}
