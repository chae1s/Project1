package com.toy.project1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.toy.project1.service.MyUserDetailsService;



import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final MyUserDetailsService myUserDetailsService;
	
		
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
									.antMatchers("/").permitAll()
									.antMatchers("/users/login/**").anonymous()
									.antMatchers("/users/join").anonymous()
									.antMatchers("/users/emailCheck").anonymous()
									.antMatchers("/users/nicknameCheck").anonymous()
									.antMatchers("/users/findPassword").anonymous()
									.antMatchers("/send-mail/*").anonymous()
									.antMatchers("/users/**").authenticated()
									.and()
			.formLogin()
						.loginPage("/users/login")
						.defaultSuccessUrl("/")
						.failureUrl("/users/login?error=true")
						.loginProcessingUrl("/users/login_proc")
						.usernameParameter("email")
						.permitAll()
						.and()
			.logout()
						.logoutUrl("/users/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
						.and()
			.userDetailsService(myUserDetailsService)
			.oauth2Login()
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
