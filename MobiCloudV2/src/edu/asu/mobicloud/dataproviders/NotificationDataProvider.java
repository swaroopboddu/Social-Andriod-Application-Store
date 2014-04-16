/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.rest.model.Notification;
import edu.asu.mobicloud.retrofit.RestClient;

/**
 * @author satyaswaroop
 * 
 */
public class NotificationDataProvider extends Observable {

	private static NotificationDataProvider instance;
	List<Notification> list = new ArrayList<Notification>();

	public List<Notification> getList(String token) {

		RestClient.getNotifications(token);
		return list;
	}

	public int notificationCount() {
		return list.size();
	}

	private void setList(List<Notification> list) {

		this.list.addAll(list);
	}

	public void onUpdateCallback(List<Notification> list) {
		setList(list);
		setChanged();
		notifyObservers();
	}

	public static NotificationDataProvider getInstance() {
		if (instance == null)
			instance = new NotificationDataProvider();

		return instance;
	}

	public void clear() {
		list.clear();
		setChanged();
		notifyObservers();
	}

}
