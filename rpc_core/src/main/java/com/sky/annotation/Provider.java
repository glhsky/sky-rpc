package com.sky.annotation;

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
