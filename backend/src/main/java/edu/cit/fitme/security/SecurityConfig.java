package edu.cit.fitme.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // 🔥 Fix CORS issues!
                .and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Public endpoints
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/api/google/**").permitAll()
                        .requestMatchers("/api/users/encode/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()

                        // 🔐 Secured endpoints
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/weight").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/workouts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workouts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/workouts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/workouts/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.POST, "/api/exercises").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/exercises/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/api/workout-days/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/workout-days").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workout-days/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/workout-days/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/day-exercises/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/day-exercises").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/day-exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/day-exercises/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/progress").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/progress").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/progress/today").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/progress/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/weights").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/weights").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/weights/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Unauthorized - JWT required");
                        }))
                .formLogin(form -> form.disable())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .successHandler(customOAuth2SuccessHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
