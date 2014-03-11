package edu.asu.mobicloud;

import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.listeners.TabListener;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import edu.asu.mobicloud.fragments.*;

public class MainActivity extends Activity implements ListFragmentDataProvider {
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		prefs = getSharedPreferences("edu.asu.mobicloud", Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		Tab tab = actionBar
				.newTab()
				.setText(R.string.title_applications_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "apps",
								MobiListFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.title_friends_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "friends",
								MobiListFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.title_developers_tab)
				.setTabListener(
						new TabListener<MobiListFragment>(this, "developers",
								MobiListFragment.class));
		actionBar.addTab(tab);

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

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			onSearchRequested();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Bundle getData() {
		Bundle bundle = new Bundle();
		bundle.putString("user_id", prefs.getString("user_id", null));
		return bundle;
	}

}
