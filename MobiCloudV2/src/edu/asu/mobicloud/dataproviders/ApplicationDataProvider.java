/**
 * 
 */
package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.os.AsyncTask;

import edu.asu.mobicloud.model.Application;
import edu.asu.mobicloud.model.Comment;
import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.retrofit.RestClient;

/**
 * @author satyaswaroop
 * 
 */
public class ApplicationDataProvider extends Observable {
	private static ApplicationDataProvider appProvider;
	private List<ApplicationCapsule> list=new ArrayList<ApplicationCapsule>();

	private ApplicationDataProvider() {

	}

	public static ApplicationDataProvider getInstance() {
		if (appProvider == null) {
			appProvider = new ApplicationDataProvider();
		}

		return appProvider;
	}

	public List<Application> getApps(String userId) {
		List<Application> list = new ArrayList<Application>();
		List<Comment> cList = new ArrayList<Comment>();
		Comment c = new Comment();
		c.setMessage("Awesome App");

		String name = ("Tommy");
		c.setUser(name);
		cList.add(c);
		Comment c1 = new Comment();
		c1.setMessage("Awesome App - good one");
		c1.setUser("Gimmy");
		cList.add(c1);

		for (int i = 0; i < 10; i++) {
			Application a = new Application();
			a.setComments(cList);
			a.setDescription("This is a sample Application");
			a.setDeveloper("user1");
			a.setImageUri("");
			a.setName("SampleApp" + i);
			a.setRating(3);
			a.setDownloads(2);
			list.add(a);
		}
		return list;
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
			list = result;
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

}
