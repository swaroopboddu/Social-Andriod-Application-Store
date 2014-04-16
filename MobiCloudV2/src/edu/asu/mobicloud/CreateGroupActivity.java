package edu.asu.mobicloud;

import edu.asu.mobicloud.R.id;
import edu.asu.mobicloud.fragments.MobiListFragment;
import edu.asu.mobicloud.retrofit.RestClient;
import edu.asu.mobicloud.util.PreferencesUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateGroupActivity extends Activity {

	EditText name;
	EditText description;
	Button button;

	PreferencesUtil prefsUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		prefsUtil = new PreferencesUtil(getApplicationContext(),
				MobiListFragment.TAG);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		name = (EditText) findViewById(id.groupName);
		description = (EditText) findViewById(id.groupDescription);
		button = (Button) findViewById(id.createGroupButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RestClient.createGroups(prefsUtil
						.getPreference(MobiListFragment.TOKEN), name.getText()
						.toString(), description.getText().toString());
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_group, menu);
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

}
