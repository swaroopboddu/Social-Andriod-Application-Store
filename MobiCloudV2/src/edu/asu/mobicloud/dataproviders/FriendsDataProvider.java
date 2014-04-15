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

}
