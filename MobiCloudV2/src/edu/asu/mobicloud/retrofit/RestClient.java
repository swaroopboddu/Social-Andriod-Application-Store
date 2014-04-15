package edu.asu.mobicloud.retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.client.Response;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.dataproviders.GroupDataProvider;
import edu.asu.mobicloud.rest.model.ApplicationsList;
import edu.asu.mobicloud.rest.model.Group;
import edu.asu.mobicloud.rest.model.GroupCapsule;
import edu.asu.mobicloud.rest.model.GroupsList;
import edu.asu.mobicloud.rest.model.Result;
import edu.asu.mobicloud.rest.model.User;
import edu.asu.mobicloud.rest.model.UserCapusule;
import edu.asu.mobicloud.rest.model.UsersList;

public class RestClient {
	private static final String API_URL = "http://androidgeekvm.vlab.asu.edu/webapp";
	private static final String TAG = "edu.asu.mobicloud.retrofit";
	private static final OkHttpClient okHttpClient = new OkHttpClient();
	private static RestAdapter restAdapter = null;
	static {

		okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
		okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
		restAdapter = new RestAdapter.Builder().setEndpoint(API_URL)
				.setLogLevel(LogLevel.FULL)
				.setLog(new AndroidLog("Reftofit log"))
				.setClient(new OkClient(okHttpClient)).build();
	}

	public static String login(String email, String password) {

		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Result result = null;
		try {
			result = cloud.tryLogin(email, password);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
		return result.getResult().getUser().getToken();
	}

	public static String register(String firstName, String lastName,
			String email, String password, String mobile) {

		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Result result = null;
		try {
			result = cloud.register(firstName, lastName, email, password,
					password, mobile);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
		return result.getResult().getUser().getToken();
	}

	public static String checkToken(String string) {
		if (string != null) {
			// TODO token validation
			System.out.println(string);
			return "Satya Swaroop";
		} else
			return null;
	}

	public static ApplicationsList getApplications(String token) {
		if (token != null) {
			System.out.println("In getapps:" + token);
			MobiCloud cloud = restAdapter.create(MobiCloud.class);

			try {
				return cloud.getApplications(token);
			} catch (RetrofitError e) {
				System.out.println(e.getResponse().getStatus());
			}
		}
		return null;
	}

	public static void createGroups(String token, String title,
			String description) {

		if (token != null) {
			System.out.println("In create Group:" + token);
			MobiCloud cloud = restAdapter.create(MobiCloud.class);
			Callback<GroupsList> callback = new Callback<GroupsList>() {

				@Override
				public void success(GroupsList arg0, Response arg1) {
					if (arg0.getGroups() != null && !arg0.getGroups().isEmpty())
						for (GroupCapsule gc : arg0.getGroups()) {
							gc.getGroup().getMembers().clear();
						}
					GroupDataProvider.getInstance().onUpdateCallback(
							arg0.getGroups());
				}

				@Override
				public void failure(RetrofitError arg0) {
					GroupDataProvider.getInstance().onFailure();
					Log.e(TAG, arg0.getMessage());
				}
			};
			try {
				cloud.createGroup(token, title, description, callback);
			} catch (RetrofitError e) {
				System.out.println(e.getResponse().getStatus());
			}
		}

	}

	public static void getGroups(String token) {

		if (token != null) {
			System.out.println("In get Group:" + token);
			MobiCloud cloud = restAdapter.create(MobiCloud.class);
			Callback<GroupsList> callback = new Callback<GroupsList>() {

				@Override
				public void success(GroupsList arg0, Response arg1) {
					if (arg0.getGroups() != null && !arg0.getGroups().isEmpty())
						GroupDataProvider.getInstance().onUpdateCallback(
								arg0.getGroups());
				}

				@Override
				public void failure(RetrofitError arg0) {
					/*
					 * String response = null;
					 * 
					 * TokenError r = (TokenError) arg0
					 * .getBodyAs(TokenError.class); response =
					 * r.getErrorDetails();
					 * 
					 * Log.e(TAG, response); if (response != null &&
					 * response.contains("Invalid Token ID"))
					 */
					GroupDataProvider.getInstance().onFailure();
					Log.e(TAG, arg0.getMessage());
				}
			};
			try {
				cloud.getGroups(token, callback);
			} catch (RetrofitError e) {
				Log.e(TAG, "" + e.getResponse().getStatus(), e);
			}
		}

	}

	public static void getApps() {

		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<ApplicationsList> callback = new Callback<ApplicationsList>() {

			@Override
			public void success(ApplicationsList arg0, Response arg1) {
				if (arg0.getApplications() != null
						&& !arg0.getApplications().isEmpty())
					ApplicationDataProvider.getInstance().onUpdateCallback(
							arg0.getApplications());
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getApps(callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
	}

	public static void getMembers(final Group group, String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<UsersList> callback = new Callback<UsersList>() {
			@Override
			public void success(UsersList list, Response resp) {

				List<User> usersList = new ArrayList<User>();
				for (UserCapusule uc : list.getUsers()) {
					usersList.add(uc.getUser());
				}
				if (usersList != null && !usersList.isEmpty()) {
					group.setMembers(usersList);
					GroupDataProvider.getInstance().onUpdateCallback();
				}

			}

			@Override
			public void failure(RetrofitError arg0) {
				GroupDataProvider.getInstance().onFailure();
				Log.e(TAG, arg0.getMessage());
			}
		};
		try {
			cloud.getMembers(token, group.getId(), callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
	}

	public static void followGroup(final Group group, final String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<Map<String, String>> callback = new Callback<Map<String, String>>() {

			@Override
			public void failure(RetrofitError arg0) {
				GroupDataProvider.getInstance().onFailure();
				Log.e(TAG, arg0.getMessage());
			}

			@Override
			public void success(Map<String, String> map, Response resp) {
				if (map.containsKey("result")) {
					if (map.get("result").equalsIgnoreCase("success")) {
						Log.d(TAG, "Successfully following");
						getMembers(group, token);
					}
				}
			}
		};
		try {
			cloud.followGroup(token, group.getId(), callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
	}

	public static void unFollowGroup(final Group group, final String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<Map<String, String>> callback = new Callback<Map<String, String>>() {

			@Override
			public void failure(RetrofitError arg0) {
				GroupDataProvider.getInstance().onFailure();
				Log.e(TAG, arg0.getMessage());
			}

			@Override
			public void success(Map<String, String> map, Response resp) {
				if (map.containsKey("result")) {
					if (map.get("result").contains("success")) {
						Log.d(TAG, "Successfully unfollowed");
						getMembers(group, token);
					}
				}
			}
		};
		try {
			// cloud.unFollowGroup(token, group.getId(), callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}
	}

	public static void getGroups() {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<GroupsList> callback = new Callback<GroupsList>() {

			@Override
			public void success(GroupsList arg0, Response arg1) {
				if (arg0.getClass() != null && !arg0.getGroups().isEmpty())
					GroupDataProvider.getInstance().onUpdatePublicCallback(
							arg0.getGroups());
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getGroups(callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	public static void getPublicUsersList(String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<UsersList> callback = new Callback<UsersList>() {
			List<User> users = new ArrayList<User>();

			@Override
			public void success(UsersList arg0, Response arg1) {
				if (arg0.getClass() != null && !arg0.getUsers().isEmpty()) {
					for (UserCapusule cap : arg0.getUsers()) {
						users.add(cap.getUser());
					}
				}
				FriendsDataProvider.getInstance().onUpdatePublicCallback(users);
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getPublicUsersList(token, callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	public static void getFriends(String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<UsersList> callback = new Callback<UsersList>() {
			List<User> users = new ArrayList<User>();

			@Override
			public void success(UsersList arg0, Response arg1) {
				if (arg0.getClass() != null && !arg0.getUsers().isEmpty()) {
					for (UserCapusule cap : arg0.getUsers()) {
						users.add(cap.getUser());
					}
				}
				FriendsDataProvider.getInstance().onUpdateCallback(users);
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getFriendsList(token, callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	public static void getFollowers(String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<UsersList> callback = new Callback<UsersList>() {
			List<User> users = new ArrayList<User>();

			@Override
			public void success(UsersList arg0, Response arg1) {
				if (arg0.getClass() != null && !arg0.getUsers().isEmpty()) {
					for (UserCapusule cap : arg0.getUsers()) {
						users.add(cap.getUser());
					}
				}
				DeveloperDataProvider.getInstance().onUpdateCallback(users);
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getFollowersList(token, callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	public static void getPublicFollowers(String token) {
		MobiCloud cloud = restAdapter.create(MobiCloud.class);
		Callback<UsersList> callback = new Callback<UsersList>() {
			List<User> users = new ArrayList<User>();

			@Override
			public void success(UsersList arg0, Response arg1) {
				if (arg0.getClass() != null && !arg0.getUsers().isEmpty()) {
					for (UserCapusule cap : arg0.getUsers()) {
						users.add(cap.getUser());
					}
				}
				DeveloperDataProvider.getInstance().onUpdatePublicCallback(
						users);
			}

			@Override
			public void failure(RetrofitError arg0) {
				System.out.println(arg0.getMessage());

			}
		};
		try {
			cloud.getFollowersPublicList(token, callback);
		} catch (RetrofitError e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

}
