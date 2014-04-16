package edu.asu.mobicloud;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import edu.asu.mobicloud.dataproviders.NotificationDataProvider;
import edu.asu.mobicloud.rest.model.Notification;
import edu.asu.mobicloud.util.PreferencesUtil;

public class NotificationActivity extends ListActivity implements Observer {
	List<Notification> notifications;
	ArrayAdapter<Notification> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		// Show the Up button in the action bar.
		setupActionBar();
		setTitle("notifications");
		notifications = NotificationDataProvider.getInstance().getList(
				PreferencesUtil.getToken(getApplicationContext()));
		adapter = new ArrayAdapter<Notification>(this,
				R.layout.activity_notification, notifications);
		setListAdapter(adapter);

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_refresh:
			notifications = NotificationDataProvider.getInstance().getList(
					PreferencesUtil.getToken(getApplicationContext()));
			adapter.notifyDataSetChanged();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();
	}

}
