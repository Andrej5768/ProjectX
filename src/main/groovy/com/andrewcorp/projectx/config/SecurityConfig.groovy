package com.andrewcorp.projectx.config

import com.andrewcorp.projectx.security.JwtAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@Configuration
@EnableWebSecurity
class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    SecurityConfig(
            JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/users/login").permitAll()
                                .requestMatchers("/api/public/ping").permitAll()
                                .requestMatchers("/api/users/register").permitAll()
                                .anyRequest().hasAuthority("USER")

                )
//                .exceptionHandling((exceptionHandling) ->
//                        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) ->
//                                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
//                        )
//                )
                .sessionManagement { sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
                .csrf().disable()


        return http.build();
    }

    @Bean
    FilterRegistrationBean<JwtAuthorizationFilter> mobileAppAuthorizationFilterRegistrationBean() {
        FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthorizationFilter);
        registrationBean.addUrlPatterns("/api/**");
        return registrationBean;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11)
    }
}
