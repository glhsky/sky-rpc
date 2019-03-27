package com.sky.rpc.comm.exception;

/**
 * @author bainao
 */
public class NotFoundServiceInRegistryException extends Exception {

    public NotFoundServiceInRegistryException() {
    }

    public NotFoundServiceInRegistryException(String message) {
        super(message);
    }

    public NotFoundServiceInRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundServiceInRegistryException(Throwable cause) {
        super(cause);
    }

    public NotFoundServiceInRegistryException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
