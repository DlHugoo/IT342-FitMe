package edu.cit.fitme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers("/api/auth/**").permitAll()                         // Public login
                        .requestMatchers("/api/users/encode/**").permitAll() // 👈 Allow encoding
                        .requestMatchers("/api/users/createUser").permitAll()               // Public registration
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")                  // Admin-only routes
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")       // Authenticated users


                        //Workout
                        .requestMatchers("/api/workouts/create", "/api/workouts/update/**", "/api/workouts/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/workouts/**").permitAll()

                        // Exercise
                        .requestMatchers("/api/exercises/create", "/api/exercises/update/**", "/api/exercises/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/exercises/**").permitAll()

                        // Workout Days
                        .requestMatchers("/api/workout-days/create", "/api/workout-days/update/**", "/api/workout-days/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/workout-days/**").permitAll()

                        // Day Exercises
                        .requestMatchers("/api/day-exercises/create", "/api/day-exercises/update/**", "/api/day-exercises/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/day-exercises/**").permitAll()

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
