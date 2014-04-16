package edu.asu.mobicloud;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import edu.asu.mobicloud.dataproviders.NotificationDataProvider;
import edu.asu.mobicloud.util.PreferencesUtil;

public class BaseActivity extends Activity implements Observer {
	public Button notifCount;
	private int notificationCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		// Do not iconify the widget;expand it by default
		searchView.setIconifiedByDefault(false);
		View count = menu.findItem(R.id.action_badge).getActionView();
		notifCount = (Button) count.findViewById(R.id.notif_count);
		notifCount.setText(String.valueOf(notificationCount));
		notifCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNotifCount(0);
				Intent intentNotifications = new Intent(
						getApplicationContext(), NotificationActivity.class);
				startActivity(intentNotifications);
			}
		});
		notificationCount = NotificationDataProvider.getInstance()
				.getList(PreferencesUtil.getToken(getApplicationContext()))
				.size();
		NotificationDataProvider.getInstance().addObserver(this);
		return true;

	}

	private void setNotifCount(int count) {
		notificationCount = count;
		invalidateOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search: {
			onSearchRequested();
			return true;
		}
		case R.id.action_creategroup: {
			Intent intentCreateGroup = new Intent(this,
					CreateGroupActivity.class);
			startActivity(intentCreateGroup);
			return true;
		}
		case R.id.action_logout: {
			PreferencesUtil.removeToken(getApplicationContext());
			Intent intentLogin = new Intent(this, LoginActivity.class);
			startActivity(intentLogin);
			finish();
			return true;
		}
		case R.id.action_badge: {
			setNotifCount(0);
			Intent intentNotifications = new Intent(this,
					NotificationActivity.class);
			startActivity(intentNotifications);
			return true;
		}

		}
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		notificationCount = NotificationDataProvider.getInstance()
				.notificationCount();
		notifCount.setText(String.valueOf(notificationCount));
	}

}
