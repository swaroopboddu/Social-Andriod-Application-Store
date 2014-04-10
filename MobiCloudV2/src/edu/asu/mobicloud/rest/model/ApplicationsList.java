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
public class ApplicationsList {
	@SerializedName("applications")
	private List<ApplicationCapsule> applications;

	public List<ApplicationCapsule> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationCapsule> applications) {
		this.applications = applications;
	}

}
