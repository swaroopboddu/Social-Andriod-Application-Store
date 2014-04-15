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

	public List<User> getDeveloperList(String token) {
		RestClient.getFollowers(token);
		return developerList;
	}

	private void setDeveloperList(List<User> developerList) {
		this.developerList.clear();
		developerList.addAll(developerList);
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

}
