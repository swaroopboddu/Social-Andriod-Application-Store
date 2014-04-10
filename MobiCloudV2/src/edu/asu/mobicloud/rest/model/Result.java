package edu.asu.mobicloud.rest.model;

import com.google.gson.annotations.Expose;

public class Result {

	@Expose
	private LoginResult result;

	public LoginResult getResult() {
		return result;
	}

	public void setResult(LoginResult result) {
		this.result = result;
	}
}
