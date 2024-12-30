package org.bayl.runtime.exception;

import org.bayl.runtime.ZemObject;

public class ReturnException extends RuntimeException {
    private static final long serialVersionUID = -667377947471909097L;

    private ZemObject ret;

    public ReturnException(ZemObject ret) {
        this.ret = ret;
    }

    public ZemObject getReturn() {
        return ret;
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
