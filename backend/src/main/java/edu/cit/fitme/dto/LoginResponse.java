package edu.cit.fitme.dto;

public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;

    public LoginResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}