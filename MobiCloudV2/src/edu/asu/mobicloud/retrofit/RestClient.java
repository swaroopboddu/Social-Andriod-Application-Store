package edu.asu.mobicloud.retrofit;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

import com.squareup.okhttp.OkHttpClient;

import edu.asu.mobicloud.rest.model.ApplicationsList;
import edu.asu.mobicloud.rest.model.Result;

public class RestClient {
	private static final String API_URL = "http://androidgeekvm.vlab.asu.edu/webapp";
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

}
