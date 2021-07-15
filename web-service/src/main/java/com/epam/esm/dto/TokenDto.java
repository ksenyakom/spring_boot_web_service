package com.epam.esm.dto;

import com.epam.esm.model.Model;

public class TokenDto implements Model {
    private String email;
    private String token;

    public TokenDto() {
    }

    public TokenDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
