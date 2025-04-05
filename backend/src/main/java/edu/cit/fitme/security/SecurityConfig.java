package edu.cit.fitme.security;

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

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Public
                        .requestMatchers("/api/users/encode/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Registration
                        .requestMatchers("/api/login").permitAll() // Login (if you use it)

                        // ✅ USERS
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // ✅ WORKOUTS
                        .requestMatchers(HttpMethod.POST, "/api/workouts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workouts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/workouts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/workouts/**").hasAnyRole("ADMIN", "USER")

                        // ✅ EXERCISES
                        .requestMatchers(HttpMethod.POST, "/api/exercises").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/exercises/**").hasAnyRole("ADMIN", "USER")

                        // ✅ WORKOUT DAYS
                        .requestMatchers(HttpMethod.GET, "/api/workout-days/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/workout-days").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workout-days/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/workout-days/**").hasRole("ADMIN")

                        // ✅ WORKOUT DAY EXERCISES
                        .requestMatchers(HttpMethod.GET, "/api/day-exercises/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/day-exercises").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/day-exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/day-exercises/**").hasRole("ADMIN")

                        // ✅ Catch-all for secured routes
                        .anyRequest().authenticated()
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
