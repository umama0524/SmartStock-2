package com.smartstock.dto;

public class LoginResponse {
    
    private String token;
    private UserDto user;

    public LoginResponse() {}

    public LoginResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    // Builder
    public static LoginResponseBuilder builder() { return new LoginResponseBuilder(); }

    public static class LoginResponseBuilder {
        private String token;
        private UserDto user;

        public LoginResponseBuilder token(String token) { this.token = token; return this; }
        public LoginResponseBuilder user(UserDto user) { this.user = user; return this; }

        public LoginResponse build() {
            return new LoginResponse(token, user);
        }
    }
}
