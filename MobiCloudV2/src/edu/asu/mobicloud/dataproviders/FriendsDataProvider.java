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
public class FriendsDataProvider extends Observable {

	private static FriendsDataProvider fProvider;
	private List<User> list = new ArrayList<User>();
	private List<User> publicList = new ArrayList<User>();
	private List<User> usersList = new ArrayList<User>();

	private void setUsersList(List<User> usersList) {
		this.usersList.clear();
		this.usersList.addAll(usersList);
	}

	private void setPublicList(List<User> publicList) {

		this.publicList.clear();
		this.publicList.addAll(publicList);
	}

	private FriendsDataProvider() {

	}

	public static FriendsDataProvider getInstance() {
		if (fProvider == null) {
			fProvider = new FriendsDataProvider();
		}

		return fProvider;
	}

	public void onUpdateCallback(List<User> list) {
		if (list != null && !list.isEmpty()) {
			setList(list);
			setChanged();
			notifyObservers(this.list);
		}
	}

	public void onUpdatePublicCallback(List<User> list) {
		if (list != null && !list.isEmpty()) {
			setPublicList(list);
			setChanged();
			notifyObservers(this.list);
		}
	}

	public List<User> getList(String token) {
		RestClient.getFriends(token);
		return list;
	}

	private void setList(List<User> list) {
		this.list.clear();
		this.list.addAll(list);
	}

	public List<User> getPublicList(String token) {
		RestClient.getPublicUsersList(token);
		return publicList;
	}

	public List<User> getUserFriends(String token, String userId) {
		usersList.clear();
		RestClient.getUserFriends(token, userId);
		return usersList;
	}

	public boolean isFriend(User user) {
		if (list != null)
			return list.contains(user);
		else
			return false;
	}

	public void updateRelation(String token, User user) {
		RestClient.getUser(token, user);
	}

	public void onFailure() {
		setChanged();
		notifyObservers("Login Fail");
	}

	public void unFriend(String token, User user) {
		RestClient.unFriend(token, user);

	}

	public void addFriend(String token, User user) {
		RestClient.addFriend(token, user);

	}

	public void onUpdateCallback() {
		setChanged();
		notifyObservers();
	}

	public void onUpdateUsersCallback(List<User> users) {
		if (list != null && !list.isEmpty()) {
			setUsersList(list);
			setChanged();
			notifyObservers(this.usersList);
		}
	}

	public void accept(String token, User user) {
		RestClient.acceptFriend(token, user);

	}

	public void reject(String token, User user) {
		RestClient.rejectFriend(token, user);

	}

}
