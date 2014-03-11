/**
 * 
 */
package edu.asu.mobicloud.model;

import java.util.List;

/**
 * @author satyaswaroop
 * 
 */
public class Application implements ListData {
	String name;
	String id;
	String developer;
	String description;
	String imageUri;
	List<Comment> comments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String getData() {
		return name;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	@Override
	public String getImageUri() {
		return imageUri;
	}

}
