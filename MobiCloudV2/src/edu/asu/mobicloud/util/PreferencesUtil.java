package edu.asu.mobicloud.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	SharedPreferences settings;
	private static final String TOKEN = "edu.asu.mobicloud.authenticator.token";
	private static final String TAG = "edu.asu.mobicloud.LoginActivity";

	public PreferencesUtil(Context context, String prefsName) {
		settings = context
				.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
	}

	public static String getToken(Context context) {

		SharedPreferences settings = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		return settings.getString(TOKEN, null);
	}

	public static void setToken(Context context, String token) {

		SharedPreferences settings = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		settings.edit().putString(TOKEN, null).commit();
	}

	public static void removeToken(Context context) {

		SharedPreferences settings = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		settings.edit().remove(TOKEN).commit();
	}

	public void update(String name, String value) {

		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public String getPreference(String key) {

		return settings.getString(key, null);

	}
}
