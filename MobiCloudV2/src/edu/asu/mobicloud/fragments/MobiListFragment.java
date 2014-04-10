package edu.asu.mobicloud.fragments;

import java.util.ArrayList;
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
import edu.asu.mobicloud.UserDetailsActivity;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.dataproviders.GroupDataProvider;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.model.ListData;
import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.util.PreferencesUtil;

public class MobiListFragment extends ListFragment implements Observer {
	ListAdapter adapter;
	ListFragmentDataProvider provider;
	List<? extends ListData> data = null;
	List<ApplicationCapsule> data2 = null;
	ApplicationDataProvider appProvider = ApplicationDataProvider.getInstance();
	DeveloperDataProvider devProvider = DeveloperDataProvider.getInstance();
	FriendsDataProvider fProvider = FriendsDataProvider.getInstance();
	GroupDataProvider groupProvider = GroupDataProvider.getInstance();
	public static final String TOKEN = "edu.asu.mobicloud.authenticator.token";
	private static final String TAG = "edu.asu.mobicloud.LoginActivity";
	PreferencesUtil prefUtil;

	public MobiListFragment() {

		super();
		appProvider.addObserver(this);
		// TODO Auto-generated constructor stub
	}

	/*
	 * static ListData[] numbers_text = new ListData[10]; static int i = 0;
	 * static { for (int i = 0; i < 10; i++) { numbers_text[i] = new ListData();
	 * numbers_text[i].setImageUri(""); numbers_text[i].setData("" + i);
	 * 
	 * } }
	 */
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
		if (tag.equalsIgnoreCase("apps")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					DetailsActivity.class);
			intent.putExtra("application", (Parcelable) data2.get((int) id));
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("friends")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					UserDetailsActivity.class);
			intent.putExtra("name", data.get((int) id).getData());
			this.getActivity().startActivity(intent);

		}
		if (tag.equalsIgnoreCase("developers")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					DeveloperDetailsActivity.class);
			intent.putExtra("name", data.get((int) id).getData());
			this.getActivity().startActivity(intent);

		}

		if (tag.equalsIgnoreCase("groups")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(),
					GroupDetailsActivity.class);
			intent.putExtra("name", (Parcelable) data.get((int) id));
			this.getActivity().startActivity(intent);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String tag = this.getTag();
		if (tag.equalsIgnoreCase("apps")) {
			// data =
			// appProvider.getApps(provider.getData().getString("user_id"));
			data2 = appProvider.getInstalledApps(prefUtil.getPreference(TOKEN));
			adapter = new ListAdapter(inflater.getContext(), data2);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("friends")) {
			data = fProvider
					.getFriends(provider.getData().getString("user_id"));
			adapter = new ListAdapter(inflater.getContext(), data);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("developers")) {
			data = devProvider.getDevelopers(provider.getData().getString(
					"user_id"));
			adapter = new ListAdapter(inflater.getContext(), data);
			setListAdapter(adapter);
		} else if (tag.equalsIgnoreCase("groups")) {
			data = groupProvider.getGroups(provider.getData().getString(
					"user_id"));
			adapter = new ListAdapter(inflater.getContext(), data);
			setListAdapter(adapter);
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (data2 != null) {
			data2.clear();
			ArrayList<ApplicationCapsule> temp = (ArrayList<ApplicationCapsule>) arg1;
			for (ApplicationCapsule app : temp) {
				data2.add(app);
			}
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
