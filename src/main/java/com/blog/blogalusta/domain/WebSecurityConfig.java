package com.blog.blogalusta.domain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.blog.blogalusta.domain.UserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
        @Autowired
        private UserService userService;

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(authorize -> authorize

                                                .requestMatchers(antMatcher("/signup")).permitAll()
                                                .requestMatchers(antMatcher("/saveuser")).permitAll()
                                                .requestMatchers(antMatcher("/posts")).permitAll()
                                                .requestMatchers(antMatcher("/edit")).permitAll()

                                                .requestMatchers(antMatcher("/h2-console/**")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/add")).hasRole("USER")
                                                .requestMatchers(antMatcher("/update")).hasRole("USER")

                                                .anyRequest().authenticated()

                                )

                                .headers(headers -> headers
                                                .frameOptions(frameoptions -> frameoptions.disable()))
                                .formLogin(formlogin -> formlogin
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/posts", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .permitAll()
                                                .logoutSuccessUrl("/posts"));

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
        }
}