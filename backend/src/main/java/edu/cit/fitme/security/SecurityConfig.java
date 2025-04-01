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

                        // 🔓 Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/createUser").permitAll()
                        .requestMatchers("/api/users/encode/**").permitAll()

                        // 👑 Admin-only User Management
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN")

                        // ✅ WORKOUTS
                        .requestMatchers(HttpMethod.POST, "/api/workouts/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workouts/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/workouts/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/workouts/**").hasAnyRole("ADMIN", "USER")

                        // ✅ EXERCISES
                        .requestMatchers(HttpMethod.POST, "/api/exercises/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exercises/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exercises/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/exercises/**").hasAnyRole("ADMIN", "USER")

                        // ✅ WORKOUT DAYS
                        .requestMatchers(HttpMethod.DELETE, "/api/workout-days/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/workout-days/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/workout-days/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/workout-days/**").hasAnyRole("ADMIN", "USER")

                        // ✅ WORKOUT DAY EXERCISES
                        .requestMatchers(HttpMethod.POST, "/api/day-exercises/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/day-exercises/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/day-exercises/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/day-exercises/**").hasAnyRole("ADMIN", "USER")

                        // ✅ GENERAL USER ENDPOINTS
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")

                        // 🔐 Catch-all
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
