package edu.asu.mobicloud.rest.model;

import com.google.gson.annotations.SerializedName;

public class UserCapusule {
	@SerializedName("User")
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
