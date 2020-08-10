package com.example.loteria;

import java.io.Serializable;

class UserCredentials implements Serializable {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
