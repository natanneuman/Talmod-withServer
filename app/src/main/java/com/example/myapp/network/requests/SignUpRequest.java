package com.example.myapp.network.requests;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

    @SerializedName("username")
    private String email;

    @SerializedName("password")
    private String password;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
