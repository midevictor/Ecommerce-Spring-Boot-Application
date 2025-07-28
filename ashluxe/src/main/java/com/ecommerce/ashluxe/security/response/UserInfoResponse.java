package com.ecommerce.ashluxe.security.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserInfoResponse {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String jwToken;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private List<String> roles;

//    public String getUsername() {
//        return username;
//    }

    public UserInfoResponse(Long id,  String username, String jwToken, List<String> roles) {
        this.id = id;
        this.username = username;
        this.jwToken = jwToken;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;

    }

//    public void setUsername(String username) {
//        this.username = username;
//    }

//    public String getJwToken() {
//        return jwToken;
//    }
//
//    public void setJwToken(String jwToken) {
//        this.jwToken = jwToken;
//    }

//    public List<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = roles;
//    }


}

