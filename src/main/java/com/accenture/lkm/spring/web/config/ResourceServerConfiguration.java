package com.accenture.lkm.spring.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "my_rest_api";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		System.out.println("From here....");
		http.anonymous().disable().requestMatchers().antMatchers("/emp/**").and().authorizeRequests()
				.antMatchers("/emp/controller/getDetails/**").access("hasRole('ADMIN') or hasRole('USER') ")
				.antMatchers("/emp/controller/getDetailsById/**").access("hasRole('ADMIN') or hasRole('USER')")
				.antMatchers("/emp/controller/addEmp/**").access("hasRole('ADMIN')")
				.antMatchers("/emp/controller/updateEmp/**").access("hasRole('ADMIN')")
				.antMatchers("/emp/controller/deleteEmp/**").access("hasRole('ADMIN')").and().exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}

/*
 * @EnableResourceServer is a convenient annotation for OAuth2 Resource Servers,
 * it enables a Spring Security filter that authenticates requests via an
 * incoming OAuth2 token.
 * 
 * The configure(HttpSecurity http) method configures the access rules and
 * request matchers (path) for protected resources using the HttpSecurity class.
 * We secure the URL paths /emp/*, only allowing authenticated users who have
 * DUMMY_ADMIN role to access it. The server hosting the protected resources,
 * capable of accepting and responding to protected resource requests using
 * access tokens
 */
