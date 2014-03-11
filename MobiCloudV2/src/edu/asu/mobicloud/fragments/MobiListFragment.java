package edu.asu.mobicloud.fragments;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import edu.asu.mobicloud.DetailsActivity;
import edu.asu.mobicloud.UserDetailsActivity;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.model.ListData;

public class MobiListFragment extends ListFragment {
	ListFragmentDataProvider provider;
	ApplicationDataProvider appProvider = ApplicationDataProvider.getInstance();
	DeveloperDataProvider devProvider = DeveloperDataProvider.getInstance();
	FriendsDataProvider fProvider = FriendsDataProvider.getInstance();

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
		super.onAttach(activity);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String tag = this.getTag();
		if (tag.equalsIgnoreCase("apps")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(), DetailsActivity.class);
			this.getActivity().startActivity(intent);
			
		}
		if (tag.equalsIgnoreCase("friends")) {
			Intent intent = new Intent(this.getActivity().getBaseContext(), UserDetailsActivity.class);
			this.getActivity().startActivity(intent);
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		List<? extends ListData> numbers_text = null;
		String tag = this.getTag();
		if (tag.equalsIgnoreCase("apps")) {
			numbers_text = appProvider.getApps(provider.getData().getString(
					"user_id"));
		} else if (tag.equalsIgnoreCase("friends")) {
			numbers_text = fProvider.getFriends(provider.getData().getString(
					"user_id"));
		} else if (tag.equalsIgnoreCase("developers")) {
			numbers_text = devProvider.getDevelopers(provider.getData()
					.getString("user_id"));
		}
		ListAdapter adapter = new ListAdapter(inflater.getContext(),
				 numbers_text);
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
