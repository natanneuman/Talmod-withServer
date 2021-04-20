package com.example.myapp.network.responses.userResponses;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("username")
	private String email;

	@SerializedName("token")
	private String token;

	public int getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public String getToken(){
		return token;
	}
}