package com.example.lms.common.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = MockCustomUserSecurityContextFactory.class)
public @interface WithMockCustom {
    long id() default 1L;
    String role();
}