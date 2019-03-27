package com.sky.exception;

/**
 * @author bainao
 */
public class NotFoundAvailableServerInRegistry extends Exception {

    public NotFoundAvailableServerInRegistry() {
    }

    public NotFoundAvailableServerInRegistry(String message) {
        super(message);
    }

    public NotFoundAvailableServerInRegistry(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAvailableServerInRegistry(Throwable cause) {
        super(cause);
    }

    public NotFoundAvailableServerInRegistry(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
