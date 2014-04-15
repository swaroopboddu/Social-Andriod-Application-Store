package edu.asu.mobicloud;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.asu.mobicloud.R.id;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.GroupDataProvider;
import edu.asu.mobicloud.rest.model.GroupCapsule;
import edu.asu.mobicloud.rest.model.User;
import edu.asu.mobicloud.retrofit.RestClient;
import edu.asu.mobicloud.util.PreferencesUtil;

public class GroupDetailsActivity extends ListActivity implements Observer {
	String groupId;
	GroupCapsule group;
	ListAdapter adapter;
	PreferencesUtil prefUtils;
	List<User> members;
	Button follow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_group_details);
		super.onCreate(savedInstanceState);
		prefUtils = new PreferencesUtil(getApplicationContext(),
				LoginActivity.TAG);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			group = extras.getParcelable("name");
			System.out.println("Group ID:" + group.getGroup().getId());
		}
		if (group.getGroup().getMembers() != null
				&& (group.getGroup().getMembers().size() <= 1
						|| group.getGroup().getMembers().isEmpty() || containsNull()))
			RestClient.getMembers(this.group.getGroup(),
					prefUtils.getPreference(LoginActivity.TOKEN));
		members = group.getGroup().getMembers();
		adapter = new ListAdapter(getApplicationContext(), members);
		setListAdapter(adapter);
		TextView groupName = (TextView) findViewById(R.id.groupName);
		TextView ownerName = (TextView) findViewById(R.id.ownerName);
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(group.getGroup().getDescription());
		groupName.setText(group.getGroup().getName());
		ownerName.setText(group.getGroup().getOwner().getLastName() + ","
				+ group.getGroup().getOwner().getFirstName());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		GroupDataProvider.getInstance().addObserver(this);
		follow = (Button) findViewById(id.followButton);
		follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RestClient.followGroup(group.getGroup(),
						PreferencesUtil.getToken(getApplicationContext()));
			}
		});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO: on click of a username one should open the userdetails view
		super.onListItemClick(l, v, position, id);
	}

	private boolean containsNull() {
		for (User u : group.getGroup().getMembers()) {
			if (u.getFirstName() == null || u.getLastName() == null)
				return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_details, menu);
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
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String && arg1.equals("Login Fail")) {
			Intent intentLogin = new Intent(this, LoginActivity.class);
			startActivity(intentLogin);
			finish();

		} else {
			members.clear();
			members.addAll(group.getGroup().getMembers());
			System.out.println(""
					+ prefUtils.getPreference(LoginActivity.EXTRA_EMAIL));
			if (members.contains(new User(prefUtils
					.getPreference(LoginActivity.EXTRA_EMAIL)))) {
				follow.setText("unfollow");
				follow.setEnabled(true);
				if (group
						.getGroup()
						.getOwner()
						.getEmail()
						.equalsIgnoreCase(
								prefUtils
										.getPreference(LoginActivity.EXTRA_EMAIL))) {
					follow.setEnabled(false);
					follow.setText("Disabled");
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}
