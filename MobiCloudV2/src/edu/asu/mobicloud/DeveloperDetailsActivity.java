package edu.asu.mobicloud;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.asu.mobicloud.R.id;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.rest.model.User;
import edu.asu.mobicloud.util.PreferencesUtil;

public class DeveloperDetailsActivity extends ListActivity implements Observer {
	User developer;
	List<ApplicationCapsule> list;
	ListAdapter adapter;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developer_details);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			developer = extras.getParcelable("name");
			System.out.println("Developer ID:" + developer.getId());

		}
		button = (Button) findViewById(id.followButton);
		if (DeveloperDataProvider.getInstance().getDeveloperList()
				.contains(developer))
			button.setText("UnFollow");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (button.getText().toString().equalsIgnoreCase("Follow")) {
					DeveloperDataProvider.getInstance().follow(
							PreferencesUtil.getToken(getApplicationContext()),
							developer);
					button.setText("UnFollow");
				} else {
					DeveloperDataProvider.getInstance().unFollow(
							PreferencesUtil.getToken(getApplicationContext()),
							developer);
					button.setText("follow");
				}
			}
		});

		TextView name = (TextView) findViewById(R.id.developerName);
		name.setText(developer.getLastName() + "," + developer.getFirstName());
		populateData();
		adapter = new ListAdapter(getApplicationContext(), list);
		setListAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ApplicationDataProvider.getInstance().addObserver(this);

	}

	private void populateData() {
		// TODO Call the api method which returns list of applications
		list = ApplicationDataProvider.getInstance().getAppsByUserId(
				PreferencesUtil.getToken(getApplicationContext()),
				developer.getId());

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this.getBaseContext(), DetailsActivity.class);
		intent.putExtra("application", (Parcelable) list.get((int) id));
		this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.developer_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

}
