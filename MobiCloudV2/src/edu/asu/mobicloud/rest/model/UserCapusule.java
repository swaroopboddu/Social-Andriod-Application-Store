package edu.asu.mobicloud.rest.model;

import com.google.gson.annotations.SerializedName;

import edu.asu.mobicloud.model.ListData;

public class UserCapusule implements ListData {
	@SerializedName("User")
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return user.getLastName() + "," + user.getFirstName();
	}

	@Override
	public String getImageUri() {
		// TODO Auto-generated method stub
		return null;
	}

}
