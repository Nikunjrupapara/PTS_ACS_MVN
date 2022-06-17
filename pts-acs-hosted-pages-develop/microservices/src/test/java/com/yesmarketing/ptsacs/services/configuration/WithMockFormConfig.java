package com.yesmarketing.ptsacs.services.configuration;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockFormConfigSecurityContextFactory.class)
public @interface WithMockFormConfig {
    String company() default "ymnewsolutions";

    String code() default "VALIDTESTFORM";
}
