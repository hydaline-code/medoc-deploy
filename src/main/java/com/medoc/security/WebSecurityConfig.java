package com.medoc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig {
	
	@Bean
	UserDetailsService userDetailsService() {
		return new MedocUserDetailsService();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
SecurityFilterChain configureHttp(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider());

    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/register","/users/check_email","/create_user","/verify","/forgot_password","/reset_password").permitAll()
            .requestMatchers("/users/**").hasAuthority("Admin")
            // Allow Admin, Client and Pharmacie roles to post questions/answers
            .requestMatchers("/post_question/**","/postQuestionAnswer/**").hasAnyAuthority("Admin", "Client", "Pharmacie")
            .requestMatchers("/medocs/**").hasAuthority("Client")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("email")
            .defaultSuccessUrl("/", true) 
            .permitAll())

        .logout(logout -> logout.permitAll())

        .rememberMe(rem -> rem
                .key("AbcDefgHijKlmnOpqrs_1234567890")
                .tokenValiditySeconds(7 * 24 * 60 * 60));

        return http.build();
}

	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**");
	}

}
