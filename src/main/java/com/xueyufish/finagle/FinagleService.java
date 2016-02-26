package com.xueyufish.finagle;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface FinagleService {

    String name();

    FinagleProtocol[] protolcol() default FinagleProtocol.HTTP;

    String path() default "";
}
