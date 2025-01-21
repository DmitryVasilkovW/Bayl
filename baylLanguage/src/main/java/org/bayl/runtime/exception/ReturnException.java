package org.bayl.runtime.exception;

import org.bayl.runtime.BaylObject;

public class ReturnException extends RuntimeException {
    private static final long serialVersionUID = -667377947471909097L;

    private BaylObject ret;

    public ReturnException(BaylObject ret) {
        this.ret = ret;
    }

    public BaylObject getReturn() {
        return ret;
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
