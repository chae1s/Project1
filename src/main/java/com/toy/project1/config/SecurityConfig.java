package com.toy.project1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.toy.project1.service.MyUserDetailsService;

import org.springframework.security.core.userdetails.User;


import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("admin@trip.com")
				.password(encoder().encode("admin1230"))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf().disable()
			.authorizeHttpRequests()
									.antMatchers("/").permitAll()
									.and()
			.formLogin()
						.loginPage("/users/login")
						.defaultSuccessUrl("/")
						.failureUrl("/users/login")
						.loginProcessingUrl("/users/login_proc")
						.usernameParameter("email")
						.permitAll()
						.and()
			.userDetailsService(userDetailsService);
									
		;
		
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer customizer() {
		return (web) -> web.ignoring().antMatchers("/css/**", "/js/**", "/icons/**", "/images/**");
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
