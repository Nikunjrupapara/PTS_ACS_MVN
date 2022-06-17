package com.yesmarketing.ptsacs.common.configuration;

import com.yesmarketing.ptsacs.admin.util.AdminConstants;
import com.yesmarketing.ptsacs.common.authentication.JwtAuthenticationEntryPoint;
import com.yesmarketing.ptsacs.common.authentication.JwtAuthenticationFilter;
import com.yesmarketing.ptsacs.common.authentication.JwtTokenProvider;
import com.yesmarketing.ptsacs.common.util.logging.ElasticsearchAnalysisFilter;
import com.yesmarketing.ptsacs.services.authentication.ServicesAuthenticationEntryPoint;
import com.yesmarketing.ptsacs.services.authentication.ServicesAuthenticationProvider;
import com.yesmarketing.ptsacs.services.authentication.ServicesFilterChainExceptionHandler;
import com.yesmarketing.ptsacs.services.configuration.ServicesAuthenticationFilter;
import com.yesmarketing.ptsacs.services.util.MdcLoggingFilter;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
	securedEnabled = true,
	jsr250Enabled = true,
	prePostEnabled = true
)
@Slf4j
public class AuthenticationConfiguration {

	@Value("${spring.boot.admin.username}")
	private String sbAdminUser;

	@Value("${spring.boot.admin.password}")
	private String sbAdminPwd;

	@Value("${admin.ui.pwd}")
	private String adminUiPwd;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		if (!StringUtils.hasText(sbAdminUser)) {
			LOG.warn("Spring Boot Admin User (spring.boot.admin.user) property not set!");
		}
		if (!StringUtils.hasText(sbAdminPwd)) {
			LOG.warn("Spring Boot Admin Password (spring.boot.admin.password) property not set!");
		}

		UserDetails sbAdminUser = User.withUsername(this.sbAdminUser).password("{noop}"+sbAdminPwd).roles("ACTUATOR").build();

		UserDetails adminUiUser = User.withUsername("admin").password("{noop}"+adminUiPwd).roles("USER", "ADMIN").build();

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(sbAdminUser);
		manager.createUser(adminUiUser);
		return manager;
	}

	@Configuration
	@Order(1)
	@Slf4j
	public static class AuthenticateConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			LOG.info("AuthenticateConfiguration: configuring http...");
			http
					.antMatcher("/authenticate/**")
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/authenticate/**")
					.permitAll();
		}

		@Override
		public void configure(WebSecurity web) {
			LOG.info("AuthenticateConfiguration: configuring web...");
			web.ignoring().antMatchers(/*"/authenticate/**",*/
				"/favicon.ico",
				"/** /*.png ",
				"/** /*.gif",
				"/** /*.svg",
				"/** /*.jpg",
				"/** /*.html",
				"/** /*.css",
				"/** /*.js");
		}
	}

	@Configuration
	@Order(2)
	@Slf4j
	public static class ServicesAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

		@Bean
		public FilterRegistrationBean<ServicesAuthenticationFilter> servicesAuthenticationFilter() throws Exception {
			FilterRegistrationBean<ServicesAuthenticationFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			ServicesAuthenticationFilter filter = new ServicesAuthenticationFilter(authenticationManagerBean());
			result.setFilter(filter);
			result.setUrlPatterns(Collections.singletonList("/v1/services/**"));
			result.setName("Services Authentication Filter");
			return result;
		};

		@Bean
		public FilterRegistrationBean<ElasticsearchAnalysisFilter> elasticsearchAnalysisFilter() throws Exception {
			FilterRegistrationBean<ElasticsearchAnalysisFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			ElasticsearchAnalysisFilter filter = new ElasticsearchAnalysisFilter();
			result.setFilter(filter);
			result.setName("Elasticsearch Analysis Filter");
			return result;
		}

		@Bean(name = ServicesConstants.SERVICES_AUTH_MGR)
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			LOG.info("AuthenticationManager: {}", ServicesConstants.SERVICES_AUTH_MGR);
			return super.authenticationManagerBean();
		}

		@Bean
		public FilterRegistrationBean<MdcLoggingFilter> mdcLoggingFilterFilterRegistrationBean() {
			FilterRegistrationBean<MdcLoggingFilter> bean = new FilterRegistrationBean<>();
			bean.setEnabled(false);
			bean.setFilter(new MdcLoggingFilter());
			bean.setName("MDC Logging Request Id filter");
			return bean;
		}

		@Autowired
		private ServicesAuthenticationProvider servicesAuthenticationProvider;

		@Autowired
		private ServicesAuthenticationEntryPoint servicesAuthenticationEntryPoint;

		@Autowired
		private ServicesFilterChainExceptionHandler servicesFilterChainExceptionHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			LOG.info("Configuring http...");
			http
				.antMatcher("/v1/services/**")
				.cors().and().csrf().disable()
				//.csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.authorizeRequests()
				.antMatchers("/v1/services/**")
				.authenticated();

			http.addFilterBefore(mdcLoggingFilterFilterRegistrationBean().getFilter(), LogoutFilter.class);
			http.addFilterBefore(servicesFilterChainExceptionHandler, LogoutFilter.class);
			http.addFilterBefore(servicesAuthenticationFilter().getFilter(), BasicAuthenticationFilter.class);
			http.addFilterAfter(elasticsearchAnalysisFilter().getFilter(), ServicesAuthenticationFilter.class);
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			LOG.info("Adding servicesAuthenticationProvider...");
			auth.authenticationProvider(servicesAuthenticationProvider);
		}

		@Override
		public void configure(WebSecurity web) {
			web.ignoring().antMatchers("/resources/**");
		}
	}

	@Configuration
	@Order(3)
	@Slf4j
	public static class AdminAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

		private final JwtAuthenticationEntryPoint unauthorizedHandler;

		private final JwtTokenProvider jwtTokenProvider;

		@Autowired
		public AdminAuthenticationConfiguration(JwtAuthenticationEntryPoint unauthorizedHandler, JwtTokenProvider jwtTokenProvider) {
			this.unauthorizedHandler = unauthorizedHandler;
			this.jwtTokenProvider = jwtTokenProvider;
		}

		@Bean
		public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
			FilterRegistrationBean<JwtAuthenticationFilter> result = new FilterRegistrationBean<>();
			result.setEnabled(false);
			result.setFilter(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService()));
			result.setUrlPatterns(Collections.singletonList("/api/*"));
			result.setName("JWT Authentication Filter");
			return result;
		}

		@Bean(name = AdminConstants.ADMIN_API_AUTH_MGR)
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			LOG.info("AuthenticationManager: {}", AdminConstants.ADMIN_API_AUTH_MGR);
			return super.authenticationManagerBean();
		}

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			LOG.info("Configuring AuthenticationManager...");
			auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			LOG.info("Configuring httpSecurity...");
			http
				.antMatcher("/api/**")
				.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/api/**").authenticated();

			http.addFilterBefore(jwtAuthenticationFilter().getFilter(), UsernamePasswordAuthenticationFilter.class);
		}
	}

	@Configuration
	@Order(4)
	@Slf4j
 	public static class ActuatorAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

		@Bean(name = AdminConstants.ACTUATOR_AUTH_MGR)
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
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

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
		}
	}
}
