package com.example.foodcourtmicroservice.configuration.security;

import com.example.foodcourtmicroservice.configuration.security.jwt.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MainSecurity {

    @Autowired
    JwtEntryPoint jwtEntryPoint;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(requests -> requests
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.POST,"/foodCourt/restaurant/").hasAuthority("ADMINISTRATOR_ROLE")
                        .requestMatchers(HttpMethod.POST,"/foodCourt/plate/**").hasAuthority("PROVIDER_ROLE")
                        .requestMatchers(HttpMethod.PATCH,"/foodCourt/plate/").hasAuthority("PROVIDER_ROLE")
                        .requestMatchers(HttpMethod.PATCH, "/foodCourt/plate/status/{enabled}").hasAuthority("PROVIDER_ROLE")
                        .requestMatchers(HttpMethod.GET, "/foodCourt/pagination/restaurant").hasAuthority("CLIENT_ROLE")
                        .requestMatchers(HttpMethod.GET, "/foodCourt/pagination/plate").hasAuthority("CLIENT_ROLE")
                        .requestMatchers(HttpMethod.POST,"/foodCourt/order/").hasAuthority("CLIENT_ROLE")
                        .requestMatchers(HttpMethod.GET,"/foodCourt/pagination/order").hasAuthority("EMPLOYEE_ROLE")
                        .requestMatchers(HttpMethod.PATCH, "/foodCourt/orders/employee").hasAuthority("EMPLOYEE_ROLE")
                        .requestMatchers(HttpMethod.PATCH, "foodCourt/orders/action/ready/{id}").hasAuthority("EMPLOYEE_ROLE")
                        .anyRequest().authenticated()
                )
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
