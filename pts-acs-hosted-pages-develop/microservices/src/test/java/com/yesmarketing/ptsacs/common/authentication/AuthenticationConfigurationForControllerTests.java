package com.yesmarketing.ptsacs.common.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// This class should be used for controller unit tests that use WebMvcTest to set up a sliced environment
// containing the bare minimum config required for testing.
public class AuthenticationConfigurationForControllerTests extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().and().cors().disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }
}
