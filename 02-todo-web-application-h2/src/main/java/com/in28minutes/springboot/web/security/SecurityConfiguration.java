package com.in28minutes.springboot.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	// Optional : Use only if we are encodign the pwd
	static PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	
	//Create User - in28Minutes/dummy
	@Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth)
            throws Exception {
		// in memory auhentication with user and pwd( used with NoOpPasswordEncoder) 
		// alternate method for encoding pwd could be password(encoder.encode("dummy"))
		auth.inMemoryAuthentication().withUser("in28minutes").password(encoder.encode("dummy"))
        //auth.inMemoryAuthentication().withUser("in28minutes").password("{noop}dummy")
		//roles allowed are USER and ADMIN 
				.roles("USER", "ADMIN");
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		// Allow all to login and access h2 console
        http.authorizeRequests().antMatchers("/login", "/h2-console/**").permitAll()
        // Allow only role "USER" to access /todo url		
                .antMatchers("/", "/*todo*/**").access("hasRole('USER')").and()
                .formLogin();
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
	
	// Optional : Use only if we are encodign the pwd
	@Bean
    public static PasswordEncoder passwordEncoder() {
        return encoder;

    }	
}
