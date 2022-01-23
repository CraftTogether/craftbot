package com.github.crafttogether.craftbot;

public class Config {
    private String token;

    public Config() {
    }

    public Config setToken(String token) {
        this.token = token;
        return this;
    }

    public String getToken() {
        return token;
    }
}
