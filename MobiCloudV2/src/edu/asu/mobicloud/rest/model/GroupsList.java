package edu.asu.mobicloud.rest.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GroupsList {
	@SerializedName("result")
	private List<GroupCapsule> groups;

	public List<GroupCapsule> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupCapsule> groups) {
		this.groups = groups;
	}

}
