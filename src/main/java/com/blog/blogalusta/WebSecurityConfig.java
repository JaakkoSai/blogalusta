package com.blog.blogalusta;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.blog.blogalusta.web.UserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
        @Autowired
        private UserService userService;

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(authorize -> authorize

                                                .requestMatchers(antMatcher("/css/**")).permitAll()
                                                .requestMatchers(antMatcher("/signup")).permitAll()
                                                .requestMatchers(antMatcher("/saveuser")).permitAll()
                                                .requestMatchers(antMatcher("/posts")).permitAll()
                                                .requestMatchers(antMatcher("/add")).permitAll()
                                                .requestMatchers(antMatcher("/index")).permitAll()

                                                .requestMatchers(antMatcher("/edit")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/edit/**")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/h2-console/**")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/update")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/update/**")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/delete/{id}")).hasRole("ADMIN")
                                                .requestMatchers(antMatcher("/api/**")).authenticated()

                                                .anyRequest().authenticated()

                                )

                                .headers(headers -> headers
                                                .frameOptions(frameoptions -> frameoptions.disable()))
                                .formLogin(formlogin -> formlogin
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/index", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .permitAll()
                                                .logoutSuccessUrl("/index"))

                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
        }
}
