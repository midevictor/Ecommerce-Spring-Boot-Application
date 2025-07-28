package com.ecommerce.ashluxe.security.request;


import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
   @NotBlank
    private String username;
    @NotBlank
    private String  password;
}
