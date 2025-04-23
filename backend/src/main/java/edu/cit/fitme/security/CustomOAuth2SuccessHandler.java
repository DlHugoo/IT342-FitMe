package edu.cit.fitme.security;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomOAuth2SuccessHandler(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            @Lazy PasswordEncoder passwordEncoder,
            OAuth2AuthorizedClientService authorizedClientService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || name == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required OAuth2 user info");
            return;
        }

        // Save user if not exists
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setRole("user");
                    newUser.setPassword(passwordEncoder.encode("oauth2-login")); // Dummy password
                    return userRepository.save(newUser);
                });

        // 🔐 Get Google OAuth2 access token
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        System.out.println("🔑 Google Access Token: " + accessToken);

        // ✅ Issue your own app's JWT
        String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Redirect to frontend (or success handler) with JWT
        response.sendRedirect("/oauth2/success?token=" + jwtToken);
    }
}
