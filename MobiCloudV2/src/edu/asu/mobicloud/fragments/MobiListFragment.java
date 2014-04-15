package edu.asu.mobicloud.fragments;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import edu.asu.mobicloud.DetailsActivity;
import edu.asu.mobicloud.DeveloperDetailsActivity;
import edu.asu.mobicloud.GroupDetailsActivity;
import edu.asu.mobicloud.LoginActivity;
import edu.asu.mobicloud.UserDetailsActivity;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.dataproviders.GroupDataProvider;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.model.ListData;
import edu.asu.mobicloud.util.PreferencesUtil;

public class MobiListFragment extends ListFragment implements Observer {
	ListAdapter adapter;
	ListFragmentDataProvider provider;
	List<? extends ListData> friends = null;
	List<? extends ListData> applications = null;
	List<? extends ListData> developers = null;
	List<? extends ListData> groups = null;
	ApplicationDataProvider appProvider = ApplicationDataProvider.getInstance();
	DeveloperDataProvider devProvider = DeveloperDataProvider.getInstance();
	FriendsDataProvider fProvider = FriendsDataProvider.getInstance();
	GroupDataProvider groupProvider = GroupDataProvider.getInstance();
	public static final String TOKEN = "edu.asu.mobicloud.authenticator.token";
	public static final String TAG = "edu.asu.mobicloud.LoginActivity";
	PreferencesUtil prefUtil;

	public MobiListFragment() {

		super();
		appProvider.addObserver(this);
		devProvider.addObserver(this);
		fProvider.addObserver(this);
		groupProvider.addObserver(this);
	}

	@Override
	public void onAttach(Activity activity) {
		provider = (ListFragmentDataProvider) activity;
		prefUtil = new PreferencesUtil(getActivity().getApplicationContext(),
				TAG);

		super.onAttach(activity);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String tag = this.getTag();
		if (tag.equalsIgnoreCase("exploreapps")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					DetailsActivity.class);
			intent.putExtra("application",
					(Parcelable) applications.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("myapps")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					DetailsActivity.class);
			intent.putExtra("application",
					(Parcelable) applications.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("users")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					UserDetailsActivity.class);
			intent.putExtra("name", (Parcelable) friends.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("friends")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					UserDetailsActivity.class);
			intent.putExtra("name", (Parcelable) friends.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("developers")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					DeveloperDetailsActivity.class);
			intent.putExtra("name", (Parcelable) developers.get((int) id));
			this.getActivity().startActivity(intent);

		}

		if (tag.equalsIgnoreCase("groups")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					GroupDetailsActivity.class);
			intent.putExtra("name", (Parcelable) groups.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("exgroups")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					GroupDetailsActivity.class);
			intent.putExtra("name", (Parcelable) groups.get((int) id));
			this.getActivity().startActivity(intent);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String tag = this.getTag();
		if (tag.equalsIgnoreCase("myapps")) {
			applications = appProvider.getInstalledApps(prefUtil
					.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), applications);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("exploreapps")) {
			applications = appProvider.getApps();
			adapter = new ListAdapter(inflater.getContext(), applications);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("users")) {
			friends = (List<? extends ListData>) fProvider
					.getPublicList(prefUtil.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), friends);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("friends")) {
			friends = (List<? extends ListData>) fProvider.getList(prefUtil
					.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), friends);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("developers")) {
			developers = devProvider.getDeveloperList(prefUtil
					.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), developers);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("alldevelopers")) {
			developers = devProvider.getPublicList(prefUtil
					.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), developers);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("groups")) {
			groups = groupProvider.getList(prefUtil.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), groups);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("exgroups")) {
			groups = groupProvider.getPublicList();
			adapter = new ListAdapter(inflater.getContext(), groups);
			setListAdapter(adapter);
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String && arg1.equals("Login Fail")) {
			PreferencesUtil.removeToken(getActivity());
			Intent intentLogin = new Intent(
					this.getActivity().getBaseContext(), LoginActivity.class);
			startActivity(intentLogin);
			this.getActivity().finish();
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
