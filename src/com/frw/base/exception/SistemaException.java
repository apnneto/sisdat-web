package com.frw.base.exception;

import javax.ejb.ApplicationException;

/**
 * @author Carlos Santos
 */
@ApplicationException(rollback=true)
public class SistemaException extends Exception {

    protected Object[] messageParameters;

    public SistemaException(String message) {
        super(message);
    }

    public SistemaException(String message, Object... parameters) {
        super(message);
        this.messageParameters = parameters;
    }

    public SistemaException(String message, Throwable t) {
        super(message, t);
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }

}
