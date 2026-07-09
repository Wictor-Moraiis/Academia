package com.wictor.annotation;

import com.wictor.enums.AcaoLog;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditar {

    AcaoLog acao();

    String descricao() default "";

}