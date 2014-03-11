/**
 * 
 */
package edu.asu.mobicloud.model;

/**
 * @author satyaswaroop
 * 
 */
public class Comment {
	User user;
	String message;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
