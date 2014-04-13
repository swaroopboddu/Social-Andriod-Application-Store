package edu.asu.mobicloud.dataproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.asu.mobicloud.model.Group;
import edu.asu.mobicloud.model.User;

public class GroupDataProvider extends Observable {
	private static GroupDataProvider grpInstance;
	private List<edu.asu.mobicloud.rest.model.Group> list = new ArrayList<edu.asu.mobicloud.rest.model.Group>();

	public static GroupDataProvider getGrpInstance() {
		return grpInstance;
	}

	public static void setGrpInstance(GroupDataProvider grpInstance) {
		GroupDataProvider.grpInstance = grpInstance;
	}

	public List<edu.asu.mobicloud.rest.model.Group> getList() {
		return list;
	}

	private void setList(List<edu.asu.mobicloud.rest.model.Group> list) {
		this.list.clear();
		list.addAll(list);
	}

	private GroupDataProvider() {

	}

	public static GroupDataProvider getInstance() {
		if (grpInstance == null) {
			grpInstance = new GroupDataProvider();
		}
		return grpInstance;
	}

	public List<Group> getGroups(String userId) {
		User u = new User();
		u.setUserName("swaroop");
		u.setImageUrl("");
		List<User> l = new ArrayList<User>();
		l.add(u);
		List<Group> list = new ArrayList<Group>();
		for (int i = 0; i < 10; i++) {
			Group g = new Group();
			g.setTitle("Hello Group" + i);
			g.setId("" + i);
			g.setDiscription("Hello");
			g.setMembers(l);
			list.add(g);
		}
		return list;
	}

	public void onUpdateCallback(List<edu.asu.mobicloud.rest.model.Group> list) {
		setList(list);
		setChanged();
		notifyObservers(list);

	}

}
