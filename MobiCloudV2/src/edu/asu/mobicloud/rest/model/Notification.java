/**
 * 
 */
package edu.asu.mobicloud.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author satyaswaroop
 * 
 */
public class Notification {
	@SerializedName("description")
	String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
