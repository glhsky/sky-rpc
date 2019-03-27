package com.sky.rpc.server;

import java.lang.annotation.*;

/**
 * @author bainao
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provider {
    Class<?> interfaceClass();

    String version() default "";
}
