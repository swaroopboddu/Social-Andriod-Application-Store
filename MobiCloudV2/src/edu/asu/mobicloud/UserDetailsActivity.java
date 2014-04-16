package edu.asu.mobicloud;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.asu.mobicloud.R.id;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.fragments.UsersFragment;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.rest.model.User;
import edu.asu.mobicloud.util.PreferencesUtil;

public class UserDetailsActivity extends FragmentActivity implements
		ListFragmentDataProvider, Observer {

	TextView userName;
	Button addFriend;
	Button reject;
	Button friends;
	Button applications;
	Button groups;
	Button developers;
	User user;
	FriendsDataProvider fProvider;
	private String current;
	UsersFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fProvider = FriendsDataProvider.getInstance();

		fProvider.addObserver(this);
		setContentView(R.layout.activity_user_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			user = extras.getParcelable("name");
			System.out.println("User ID:" + user.getId());
		}
		fProvider.updateRelation(
				PreferencesUtil.getToken(getApplicationContext()), user);

		reject = (Button) findViewById(id.reject);
		reject.setVisibility(View.INVISIBLE);
		reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fProvider.reject(
						PreferencesUtil.getToken(getApplicationContext()), user);

			}
		});
		userName = (TextView) findViewById(id.UserName);
		userName.setText(user.getLastName() + "," + user.getFirstName());
		addFriend = (Button) findViewById(id.addFriendButton);
		applications = (Button) findViewById(id.appsButton);
		groups = (Button) findViewById(id.groupsButton);
		friends = (Button) findViewById(id.friendsButton);
		developers = (Button) findViewById(id.devsButton);
		if (user.getRelation() == null) {
			addFriend.setVisibility(View.INVISIBLE);
		}
		addFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (addFriend.getText().toString().equalsIgnoreCase("unFriend"))
					fProvider.unFriend(
							PreferencesUtil.getToken(getApplicationContext()),
							user);
				else if (addFriend.getText().toString().equalsIgnoreCase("Add Friend"))
					fProvider.addFriend(
							PreferencesUtil.getToken(getApplicationContext()),
							user);
				else if (addFriend.getText().toString().equalsIgnoreCase("accept")) {
					fProvider.accept(
							PreferencesUtil.getToken(getApplicationContext()),
							user);

				}

			}
		});

		if (isFriend()) {
			addFriend.setText("UnFriend");
			if (findViewById(R.id.fragment_container) != null) {
				if (savedInstanceState != null) {
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("userId", user.getId());
				current = "apps";
				fragment = new UsersFragment();
				fragment.setArguments(bundle);

				// Add the fragment to the 'fragment_container' FrameLayout
				getFragmentManager().beginTransaction()
						.add(R.id.fragment_container, fragment).commit();

				// }
			}
		}
		OnTabClickListener l = new OnTabClickListener();
		applications.setOnClickListener(l);
		groups.setOnClickListener(l);
		friends.setOnClickListener(l);
		developers.setOnClickListener(l);

	}

	private boolean isFriend() {

		return fProvider.isFriend(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class OnTabClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof Button) {
				Button b = (Button) v;
				if (b.getText().toString().equalsIgnoreCase("apps")
						&& !current.equals("apps")) {
					fragment.changeList("apps");
					current = "apps";
				} else if (b.getText().toString().equals("developers")
						&& !current.equals("dev")) {
					fragment.changeList("dev");
					current = "dev";
				} else if ((b.getText().toString()).equals("groups")
						&& !current.equals("groups")) {
					fragment.changeList("groups");
					current = "groups";
				} else if ((b.getText().toString()).equals("friends")
						&& !current.equals("friends")) {
					fragment.changeList("friends");
				}
			}

		}
	}

	@Override
	public Bundle getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (user.getRelation() != null) {
			if (user.getRelation().equalsIgnoreCase("Friends")) {
				addFriend.setText("unFriend");
				addFriend.setVisibility(View.VISIBLE);
				reject.setVisibility(View.GONE);
			} else if (user.getRelation().equalsIgnoreCase("Not Friends")) {
				addFriend.setText("add Friend");
				addFriend.setVisibility(View.VISIBLE);
			} else if (user.getRelation().equalsIgnoreCase("Request Pending")) {
				addFriend.setText("accept");
				addFriend.setVisibility(View.VISIBLE);
				reject.setVisibility(View.VISIBLE);

			} else if (user.getRelation().equalsIgnoreCase("Request Sent")) {
				addFriend.setText("request sent");
				addFriend.setVisibility(View.VISIBLE);
				addFriend.setEnabled(false);
				reject.setVisibility(View.GONE);
			}

		}
	}
}
