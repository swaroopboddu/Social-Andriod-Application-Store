/**
 * 
 */
package edu.asu.mobicloud.rest.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author satyaswaroop
 * 
 */
public class UsersList {

	@SerializedName("result")
	private List<UserCapusule> users;

	public List<UserCapusule> getUsers() {
		return users;
	}

	public void setUsers(List<UserCapusule> users) {
		this.users = users;
	}

}
