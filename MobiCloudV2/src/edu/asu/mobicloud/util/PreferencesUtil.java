package edu.asu.mobicloud.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	SharedPreferences settings;

	public PreferencesUtil(Context context, String prefsName) {
		settings = context
				.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
	}

	public void update(String name, String value) {

		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public String getPreference(String key) {

		return settings.getString(key, "");

	}
}
