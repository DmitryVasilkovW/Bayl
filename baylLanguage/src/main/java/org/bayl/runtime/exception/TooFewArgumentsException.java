package org.bayl.runtime.exception;

import org.bayl.SourcePosition;

public class TooFewArgumentsException extends BaylException {
    private static final long serialVersionUID = -8841576834370732148L;

    public TooFewArgumentsException(
            String functionName,
            int noArgsRequired,
            int noArgs,
            SourcePosition pos) {
        super("Function" + (functionName == null ? "" : " '" + functionName + "'")
                + " expects at least " + noArgsRequired
                + " arguments but got " + noArgs, pos);
    }
}
