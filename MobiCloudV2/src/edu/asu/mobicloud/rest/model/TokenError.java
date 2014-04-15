package edu.asu.mobicloud.rest.model;

import com.google.gson.annotations.SerializedName;

public class TokenError {
	@SerializedName("result")
	public String errorDetails;

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

}
