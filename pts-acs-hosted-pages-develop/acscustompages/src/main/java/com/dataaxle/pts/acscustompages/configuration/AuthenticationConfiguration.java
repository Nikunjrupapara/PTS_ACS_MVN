package com.dataaxle.pts.acscustompages.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesAuthenticationEntryPoint;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesAuthenticationFilter;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesAuthenticationProvider;
import com.dataaxle.pts.acscustompages.utils.logging.ElasticsearchAnalysisFilter;
import com.dataaxle.pts.acscustompages.utils.MdcLoggingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {

	@Configuration
	@Order(1)
	public static class HostedPagesAuthentication extends WebSecurityConfigurerAdapter {

		@Bean
		public FilterRegistrationBean<CustomPagesAuthenticationFilter> customPagesAuthenticationFilter() throws Exception {
			FilterRegistrationBean<CustomPagesAuthenticationFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			CustomPagesAuthenticationFilter filter = new CustomPagesAuthenticationFilter(authenticationManagerBean());
			result.setFilter(filter);
			result.setUrlPatterns(Collections.singletonList("/p/**"));
			result.setName("Custom Hosted Pages Authentication filter");
			return result;
		}

		@Bean
		public FilterRegistrationBean<MdcLoggingFilter> mdcLoggingFilterFilterRegistration() {
			FilterRegistrationBean<MdcLoggingFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			MdcLoggingFilter mdcLoggingFilter = new MdcLoggingFilter();
			result.setFilter(mdcLoggingFilter);
			result.setName("MDC Logging Request Id Filter");
			return result;
		}

		@Bean
		public ElasticsearchAnalysisFilter elasticsearchAnalysisFilter() {
			return new ElasticsearchAnalysisFilter();
		}

		@Bean
		public FilterRegistrationBean<ElasticsearchAnalysisFilter> elasticsearchAnalysisFilterFilterRegistrationBean() {
			FilterRegistrationBean<ElasticsearchAnalysisFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			result.setFilter(elasticsearchAnalysisFilter());
			result.setName("Elasticsearch Analysis Filter");
			return result;
		}

		@Autowired
		private HandlerExceptionResolver handlerExceptionResolver;

		@Autowired
		private CustomPagesAuthenticationProvider customPagesAuthenticationProvider;

		@Autowired
		private CustomPagesAuthenticationEntryPoint customPagesAuthenticationEntryPoint;

		@Autowired
		private ConfigurationParameters configurationParameters;

		@Override
		public void configure(WebSecurity web) throws Exception {
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http
				.authorizeRequests()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()
				.and()
				.antMatcher("/p/**")
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/p/**")
				.authenticated();

			String cspReportingUri = String.format("%s/p/page/csp/reporting", configurationParameters.getContextPath());
			String cspStr = String.format("report-to %s; report-uri %s", cspReportingUri, cspReportingUri);
			http
				.headers(headers ->
							 headers
								 .defaultsDisabled()
								 .cacheControl(withDefaults())
								 .contentTypeOptions(withDefaults())
								 .contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy.policyDirectives(cspStr))
								 .xssProtection(withDefaults())
								 .httpStrictTransportSecurity(
									 hsts -> hsts
										 .includeSubDomains(true)
										 .preload(true)
										 .maxAgeInSeconds(31536000)
								 )
				);

			http.addFilterBefore(mdcLoggingFilterFilterRegistration().getFilter(), LogoutFilter.class);
			http.addFilterBefore(customPagesAuthenticationFilter().getFilter(), UsernamePasswordAuthenticationFilter.class);
			http.addFilterAfter(elasticsearchAnalysisFilter(), CustomPagesAuthenticationFilter.class);
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(customPagesAuthenticationProvider);
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
	}

	@Configuration
	@Slf4j
	public static class ActuatorAuthentication extends WebSecurityConfigurerAdapter {

		@Value("${spring.boot.admin.username}")
		private String sbAdminUser;

		@Value("${spring.boot.admin.password}")
		private String sbAdminPwd;

		@Bean
		public UserDetailsService userDetailsService() {
			if (!StringUtils.hasText(sbAdminUser)) {
				LOG.warn("Spring Boot Admin User (spring.boot.admin.user) property not set!");
			}
			if (!StringUtils.hasText(sbAdminPwd)) {
				LOG.warn("Spring Boot Admin Password (spring.boot.admin.password) property not set!");
			}

			UserDetails sbAdminUser = User.withUsername(this.sbAdminUser).password("{noop}"+sbAdminPwd).roles("ACTUATOR").build();

			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			manager.createUser(sbAdminUser);
			return manager;
		}


			@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.userDetailsService(userDetailsService())
				.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				// do not require authentication for health endpoint
				.antMatcher("/actuator/health")
				.authorizeRequests()
				.antMatchers("/actuator/health")
				.permitAll()
				.and()
				// all other actuator endpoints should require authentication
				.antMatcher("/actuator/**")
				.csrf().ignoringAntMatchers("/actuator/**")
				.and()
				.authorizeRequests()
				.antMatchers("/actuator/**")
				.hasRole("ACTUATOR").and().httpBasic();
		}
	}
 }
