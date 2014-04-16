/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.retrofit.RestClient;

/**
 * @author satyaswaroop
 * 
 */
public class ApplicationDataProvider extends Observable {
	private static ApplicationDataProvider appProvider;
	private List<ApplicationCapsule> list = new ArrayList<ApplicationCapsule>();

	public void setList(List<ApplicationCapsule> list) {
		this.list.clear();
		if (list != null)
			this.list.addAll(list);
	}

	private ApplicationDataProvider() {

	}

	public static ApplicationDataProvider getInstance() {
		if (appProvider == null) {
			appProvider = new ApplicationDataProvider();
		}

		return appProvider;
	}

	/**
	 * This method is used to get all the installed applications.
	 * 
	 * @param tokenId
	 * @return
	 */
	public List<ApplicationCapsule> getInstalledApps(String tokenId) {
		list.clear();
		RestClient.getApplications(tokenId);
		return list;
	}

	/*
	 * private class LongOperation extends AsyncTask<String, Void,
	 * List<ApplicationCapsule>> {
	 * 
	 * @Override protected List<ApplicationCapsule> doInBackground(String...
	 * params) { List<ApplicationCapsule> appCapsule =
	 * RestClient.getApplications( params[0]).getApplications(); if (appCapsule
	 * != null) return appCapsule; else return new
	 * ArrayList<ApplicationCapsule>();
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(List<ApplicationCapsule> result) {
	 * setList(result); setChanged(); notifyObservers(list); }
	 * 
	 * @Override protected void onPreExecute() { }
	 * 
	 * @Override protected void onProgressUpdate(Void... values) { } }
	 */

	public void onUpdateCallback(
			List<edu.asu.mobicloud.rest.model.ApplicationCapsule> list) {
		if (list != null && !list.isEmpty()) {
			setList(list);
			setChanged();
			notifyObservers(this.list);
		}

	}

	public List<ApplicationCapsule> getAppsByUserId(String token,
			String developerId) {
		list.clear();
		RestClient.getUserApplications(token, developerId);
		return list;
	}

	public List<ApplicationCapsule> getApps() {
		list.clear();
		RestClient.getApps();
		return list;
	}

	public void onFailure() {
		setChanged();
		notifyObservers("Login Fail");
	}

}
