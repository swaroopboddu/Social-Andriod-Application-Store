package edu.asu.mobicloud;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import edu.asu.mobicloud.dataproviders.SearchDataProvider;
import edu.asu.mobicloud.fragments.MobiListFragment;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.listeners.TabListener;

public class SearchableActivity extends BaseActivity implements
		ListFragmentDataProvider {
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		prefs = getSharedPreferences("edu.asu.mobicloud", Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable);
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		handleIntent(intent);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tab = actionBar
				.newTab()
				.setText(R.string.title_applications_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "searchapps",
								MobiListFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.title_users_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "searchusers",
								MobiListFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.title_groups_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "searchgroups",
								MobiListFragment.class));
		actionBar.addTab(tab);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			setTitle(query);
			doMySearch(query);
		}

	}

	private void doMySearch(String query) {
		SearchDataProvider.getInstance().setSearch(query);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchable, menu);
		return true;
	}

	@Override
	public Bundle getData() {
		Bundle bundle = new Bundle();
		bundle.putString("user_id", prefs.getString("user_id", null));
		return bundle;
	}

}
