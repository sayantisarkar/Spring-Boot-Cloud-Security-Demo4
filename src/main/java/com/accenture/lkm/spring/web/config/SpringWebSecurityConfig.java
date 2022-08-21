package com.accenture.lkm.spring.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// these annotations are used to declare this file as the Security File
@Configuration
@EnableWebSecurity
public class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Responsibility to define the authorizationRules is of Resource Server hence
	// not write here any thing
	// here it is told to enable OAuth 2.0 URLs with anonymous access
	// Responsibility to trigger the Authentication and Authorization is of
	// AuthorizationServer
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().anonymous().disable().authorizeRequests().antMatchers("/oauth/token").permitAll();

	}

	// AuthenticationProviderManager Resource Owner Credentials
	// Configured users
	// Use builder pattern only to define user, don't use any other way other wise
	// it will not work
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("ADMIN").and().withUser("bob")
				.password("abc123").roles("USER");
	}

	// Registering authentication manager as Spring Bean is required
	// so only override is done, same authenticationManager will be required
	// in the Authorization server
	// Responsibility to trigger the Authentication and Authorization is of
	// AuthorizationServer
	// It will need the AuthenticationManager hence it is managed as Spring Bean
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
