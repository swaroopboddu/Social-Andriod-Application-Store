package edu.asu.mobicloud.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import edu.asu.mobicloud.model.ListData;

public class Group implements Parcelable, ListData {
	String id;
	String name;
	String description;
	@SerializedName("User")
	private User owner;
	@SerializedName("GroupsUser")
	private List<User> members;

	public Group(Parcel input) {
		id = input.readString();
		name = input.readString();
		description = input.readString();
		owner = input.readParcelable(User.class.getClassLoader());
		members = new ArrayList<User>();
		input.readTypedList(members, User.CREATOR);
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeParcelable(owner, 0);
		dest.writeList(members);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {

		@Override
		public Group createFromParcel(Parcel input) {
			// TODO Auto-generated method stub
			return new Group(input);
		}

		@Override
		public Group[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Group[size];
		}

	};

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getImageUri() {
		// TODO Auto-generated method stub
		return null;
	}

}
