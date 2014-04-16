/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.rest.model.GroupCapsule;
import edu.asu.mobicloud.rest.model.UserCapusule;
import edu.asu.mobicloud.retrofit.RestClient;

/**
 * @author satyaswaroop
 * 
 */
public class SearchDataProvider extends Observable {
	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	private SearchDataProvider() {

	}

	private static SearchDataProvider instance;
	private List<ApplicationCapsule> appsList = new ArrayList<ApplicationCapsule>();

	public void setAppsList(List<ApplicationCapsule> list) {
		this.appsList.clear();
		this.appsList.addAll(list);
	}

	private List<UserCapusule> usersList = new ArrayList<UserCapusule>();
	private List<GroupCapsule> groupsList = new ArrayList<GroupCapsule>();

	public List<GroupCapsule> getGroupsList(String token) {
		groupsList.clear();
		RestClient.searchGroups(token, search);
		return groupsList;
	}

	public void setGroupsList(List<GroupCapsule> groupsList) {
		this.groupsList.clear();
		this.groupsList.addAll(groupsList);
	}

	public static SearchDataProvider getInstance() {
		if (instance == null)
			instance = new SearchDataProvider();
		return instance;
	}

	public List<UserCapusule> searchUsers(String token) {
		usersList.clear();
		RestClient.searchUsers(token, search);
		return usersList;
	}

	public List<ApplicationCapsule> searchApps(String token) {
		appsList.clear();
		RestClient.search(token, search);
		return appsList;
	}

	public void onUpdateAppsCallback(List<ApplicationCapsule> list) {
		setAppsList(list);
		setChanged();
		notifyObservers();
	}

	public void onUpdateUsersCallback(List<UserCapusule> users) {
		setUsersList(users);
		setChanged();
		notifyObservers();
	}

	public void onUpdateGroupsCallback(List<GroupCapsule> groups) {
		setGroupsList(groups);
		setChanged();
		notifyObservers();
	}

	private void setUsersList(List<UserCapusule> users) {
		this.usersList.clear();
		this.usersList.addAll(users);

	}

	public void onFailure() {

	}

}
