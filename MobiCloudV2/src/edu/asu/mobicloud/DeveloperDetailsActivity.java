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
import android.widget.ListView;
import android.widget.TextView;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.rest.model.ApplicationCapsule;

public class DeveloperDetailsActivity extends ListActivity implements Observer {
	String developerId;
	List<ApplicationCapsule> list;
	ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			developerId = extras.getString("name");
			System.out.println("Developer ID:" + developerId);

		}

		setContentView(R.layout.activity_developer_details);
		TextView name = (TextView) findViewById(R.id.developerName);
		name.setText(developerId);
		populateData();
		adapter = new ListAdapter(getApplicationContext(), list);
		setListAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ApplicationDataProvider.getInstance().addObserver(this);

	}

	private void populateData() {
		// TODO Call the api method which returns list of applications
		list = ApplicationDataProvider.getInstance().getAppsByUserId(
				developerId);

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
