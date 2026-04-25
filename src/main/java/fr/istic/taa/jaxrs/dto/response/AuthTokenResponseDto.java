package fr.istic.taa.jaxrs.dto.response;

import java.util.Set;

public class AuthTokenResponseDto {
    private String token;
    private Set<String> roles;
    private String message;

    public AuthTokenResponseDto() {
    }

    public AuthTokenResponseDto(String token, Set<String> roles, String message) {
        this.token = token;
        this.roles = roles;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
