/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.rest.model.User;
import edu.asu.mobicloud.retrofit.RestClient;

/**
 * @author satyaswaroop
 * 
 */
public class DeveloperDataProvider extends Observable {
	private static DeveloperDataProvider devProvider;
	private List<User> developerList = new ArrayList<User>();
	private List<User> publicList = new ArrayList<User>();
	private List<User> usersList = new ArrayList<User>();

	public List<User> getDeveloperList(String token) {
		RestClient.getFollowers(token);
		return developerList;
	}

	private void setDeveloperList(List<User> developerList) {
		this.developerList.clear();
		this.developerList.addAll(developerList);
	}

	public List<User> getPublicList(String token) {
		RestClient.getPublicFollowers(token);
		return publicList;
	}

	public void setPublicList(List<User> publicList) {
		this.publicList.clear();
		this.publicList.addAll(publicList);
	}

	public List<User> getDeveloperList() {
		return developerList;
	}

	private DeveloperDataProvider() {

	}

	public static DeveloperDataProvider getInstance() {
		if (devProvider == null) {
			devProvider = new DeveloperDataProvider();
		}

		return devProvider;
	}

	public void onUpdatePublicCallback(List<User> list) {
		if (list != null && !list.isEmpty()) {
			setPublicList(list);
			setChanged();
			notifyObservers(this.publicList);
		}
	}

	public void onUpdateCallback(List<User> users) {
		if (users != null && !users.isEmpty()) {
			setDeveloperList(users);
			setChanged();
			notifyObservers(this.developerList);
		}
	}

	public void onUpdateUsersCallback(List<User> users) {
		if (users != null && !users.isEmpty()) {
			setUsersList(users);
			setChanged();
			notifyObservers(this.usersList);
		}
	}

	private void setUsersList(List<User> users) {
		usersList.clear();
		usersList.addAll(users);
	}

	public List<User> getUserFollowers(String token, String userId) {
		usersList.clear();
		RestClient.getUsersFollowers(token, userId);
		return usersList;
	}

	public void onFailure() {
		setChanged();
		notifyObservers("Login Fail");
	}

	public void follow(String token, User developer) {
		if (!developerList.contains(developer)) {
			developerList.add(developer);
			RestClient.followDeveloper(token, developer);
		}

	}

	public void unFollow(String token, User developer) {
		if (developerList.contains(developer)) {
			developerList.remove(developer);
			RestClient.unFollowDeveloper(token, developer);
		}
	}

	public void onUpdateCallback() {
		setChanged();
		notifyObservers();
	}

}
