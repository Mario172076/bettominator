package com.example.bettominator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider;

    public WebSecurityConfig(PasswordEncoder passwordEncoder, CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.customUsernameAndPasswordAuthenticationProvider = customUsernameAndPasswordAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/themes/**", "/Home", "/Contact", "/Register", "/api/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/Login").permitAll()
                .failureUrl("/Login?error=BadCredentials")
                .defaultSuccessUrl("/Home", true)
                .and()
                .logout()
                .logoutUrl("/Logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/Login")
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customUsernameAndPasswordAuthenticationProvider);
    }


}
