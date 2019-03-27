package com.sky.exception;

/**
 * @author bainao
 */
public class NotFoundRegistryException extends RuntimeException {

    public NotFoundRegistryException() {
        super();
    }

    public NotFoundRegistryException(String message) {
        super(message);
    }

    public NotFoundRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundRegistryException(Throwable cause) {
        super(cause);
    }

    protected NotFoundRegistryException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
