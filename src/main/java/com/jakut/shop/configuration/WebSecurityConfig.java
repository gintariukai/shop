package com.jakut.shop.configuration;

import com.jakut.shop.jwt.JwtAuthorizationFilter;
import com.jakut.shop.jwt.JwtTokenProvider;
import com.jakut.shop.service.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Cross-origin-resource-sharing: localhost:8080, localhost:4200(allow for it.)
        http.cors().and()
                // Never create session for security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // Configure endpoints authorization requirements
                .authorizeHttpRequests(auth -> auth
                        //These are public paths
                        .requestMatchers("/resources/**",  "/error", "/api/user/**").permitAll()
                        //These can be reachable for just have admin role.
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //All remaining paths should need authentication.
                        .anyRequest().fullyAuthenticated()
                )
                //logout submission path
                .logout(httpLogoutConfig -> httpLogoutConfig.logoutUrl("/api/user/logout").permitAll())
                //login submission path
                .formLogin(httpLoginConfig -> httpLoginConfig.loginProcessingUrl("/api/user/login"))
                //Cross side request forgery
                .csrf().disable();

        //jwt filter
        http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Profile("h2")    //when a profile for h2 (instead of mysql is used)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(H2ConsoleProperties properties) {
        return web -> web
                .ignoring()
                .requestMatchers(properties.getPath() + "/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}