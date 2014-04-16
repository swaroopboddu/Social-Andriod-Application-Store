package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.rest.model.GroupCapsule;
import edu.asu.mobicloud.retrofit.RestClient;

public class GroupDataProvider extends Observable {
	private static GroupDataProvider grpInstance;
	private List<edu.asu.mobicloud.rest.model.GroupCapsule> list = new ArrayList<edu.asu.mobicloud.rest.model.GroupCapsule>();
	private List<edu.asu.mobicloud.rest.model.GroupCapsule> publicList = new ArrayList<edu.asu.mobicloud.rest.model.GroupCapsule>();
	private List<edu.asu.mobicloud.rest.model.GroupCapsule> usersList = new ArrayList<GroupCapsule>();

	public List<edu.asu.mobicloud.rest.model.GroupCapsule> getUsersList(
			String token, String userId) {
		usersList.clear();
		RestClient.getUserGroups(token, userId);
		return usersList;
	}

	private void setUsersList(
			List<edu.asu.mobicloud.rest.model.GroupCapsule> usersList) {
		this.usersList.clear();
		this.usersList.addAll(usersList);
	}

	public List<edu.asu.mobicloud.rest.model.GroupCapsule> getPublicList() {
		RestClient.getGroups();
		return publicList;
	}

	private void setPublicList(
			List<edu.asu.mobicloud.rest.model.GroupCapsule> publicList) {
		this.publicList.clear();
		this.publicList.addAll(publicList);
	}

	public static GroupDataProvider getGrpInstance() {
		return grpInstance;
	}

	public static void setGrpInstance(GroupDataProvider grpInstance) {
		GroupDataProvider.grpInstance = grpInstance;
	}

	public List<edu.asu.mobicloud.rest.model.GroupCapsule> getList(String token) {
		RestClient.getGroups(token);
		return list;
	}

	private void setList(List<edu.asu.mobicloud.rest.model.GroupCapsule> list) {
		this.list.clear();
		this.list.addAll(list);
	}

	private GroupDataProvider() {

	}

	public static GroupDataProvider getInstance() {
		if (grpInstance == null) {
			grpInstance = new GroupDataProvider();
		}
		return grpInstance;
	}

	public void onUpdateCallback(
			List<edu.asu.mobicloud.rest.model.GroupCapsule> list) {
		if (list != null && !list.isEmpty()) {
			setList(list);
			setChanged();
			notifyObservers();
		}

	}

	public void onUpdatePublicCallback(
			List<edu.asu.mobicloud.rest.model.GroupCapsule> list) {
		if (list != null && !list.isEmpty()) {
			setPublicList(list);
			setChanged();
			notifyObservers();
		}

	}

	public void onUpdateUsersCallback(
			List<edu.asu.mobicloud.rest.model.GroupCapsule> list) {
		if (list != null && !list.isEmpty()) {
			setUsersList(list);
			setChanged();
			notifyObservers(this.usersList);
		}

	}

	public void onUpdateCallback() {
		setChanged();
		notifyObservers();
	}

	public void onFailure() {
		setChanged();
		notifyObservers("Login Fail");
	}

}
