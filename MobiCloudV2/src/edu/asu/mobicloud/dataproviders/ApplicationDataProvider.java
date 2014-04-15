/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.os.AsyncTask;
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
		if(list!=null)
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
		new LongOperation().execute(tokenId);
		return list;
	}

	private class LongOperation extends
			AsyncTask<String, Void, List<ApplicationCapsule>> {

		@Override
		protected List<ApplicationCapsule> doInBackground(String... params) {
			return RestClient.getApplications(params[0]).getApplications();

		}

		@Override
		protected void onPostExecute(List<ApplicationCapsule> result) {
			setList(result);
			setChanged();
			notifyObservers(list);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void onUpdateCallback(
			List<edu.asu.mobicloud.rest.model.ApplicationCapsule> list) {
		if (list != null && !list.isEmpty()) {
			setList(list);
			setChanged();
			notifyObservers(this.list);
		}

	}

	public List<ApplicationCapsule> getAppsByUserId(String developerId) {
		list.clear();
		// TODO: Change it to call the userId rest client later
		RestClient.getApplications(developerId).getApplications();
		return list;
	}

	public List<ApplicationCapsule> getApps() {
		list.clear();
		RestClient.getApps();
		return list;
	}

}
