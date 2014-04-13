package edu.asu.mobicloud.rest.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GroupsList {
	@SerializedName("groups")
	private List<Group> groups;

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
