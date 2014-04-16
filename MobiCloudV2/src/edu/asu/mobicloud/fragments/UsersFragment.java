package edu.asu.mobicloud.fragments;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.asu.mobicloud.LoginActivity;
import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.dataproviders.ApplicationDataProvider;
import edu.asu.mobicloud.dataproviders.DeveloperDataProvider;
import edu.asu.mobicloud.dataproviders.FriendsDataProvider;
import edu.asu.mobicloud.dataproviders.GroupDataProvider;
import edu.asu.mobicloud.interfaces.ListFragmentDataProvider;
import edu.asu.mobicloud.model.ListData;
import edu.asu.mobicloud.util.PreferencesUtil;

public class UsersFragment extends ListFragment implements Observer {
	public UsersFragment() {
		super();
		appProvider.addObserver(this);
		devProvider.addObserver(this);
		fProvider.addObserver(this);
		groupProvider.addObserver(this);
	}

	ListAdapter adapter;
	List<? extends ListData> friends = null;
	List<? extends ListData> applications = null;
	List<? extends ListData> developers = null;
	List<? extends ListData> groups = null;
	ApplicationDataProvider appProvider = ApplicationDataProvider.getInstance();
	DeveloperDataProvider devProvider = DeveloperDataProvider.getInstance();
	FriendsDataProvider fProvider = FriendsDataProvider.getInstance();
	GroupDataProvider groupProvider = GroupDataProvider.getInstance();
	ListFragmentDataProvider provider;
	private ListAdapter gAdapter;
	private ListAdapter fAdapter;
	private ListAdapter dAdapter;

	@Override
	public void onAttach(Activity activity) {
		provider = (ListFragmentDataProvider) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String userId = getArguments().getString("userId");
		String token = PreferencesUtil.getToken(getActivity());

		applications = appProvider.getAppsByUserId(token, userId);
		adapter = new ListAdapter(inflater.getContext(), applications);
		setListAdapter(adapter);

		friends = fProvider.getUserFriends(token, userId);
		fAdapter = new ListAdapter(inflater.getContext(), friends);

		developers = devProvider.getUserFollowers(token, userId);
		dAdapter = new ListAdapter(inflater.getContext(), developers);

		groups = groupProvider.getUsersList(token, userId);
		gAdapter = new ListAdapter(inflater.getContext(), groups);

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
		if (fAdapter != null)
			fAdapter.notifyDataSetChanged();
		if (gAdapter != null)
			gAdapter.notifyDataSetChanged();
		if (dAdapter != null)
			dAdapter.notifyDataSetChanged();
	}

	public void changeList(String s) {
		if (s.equals("apps")) {
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else if (s.equals("dev")) {
			setListAdapter(dAdapter);
			dAdapter.notifyDataSetChanged();
		} else if (s.equals("groups")) {
			setListAdapter(gAdapter);
			gAdapter.notifyDataSetChanged();
		} else if (s.equals("friends")) {
			setListAdapter(fAdapter);
			fAdapter.notifyDataSetChanged();
		}
	}

}
