package edu.asu.mobicloud.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import edu.asu.mobicloud.model.ListData;

public class GroupCapsule implements Parcelable, ListData {
	@SerializedName("Group")
	private Group group;

	

	public GroupCapsule(Parcel input) {
		group = input.readParcelable(Group.class.getClassLoader());

		
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	

	@Override
	public String getData() {
		return group.name;
	}

	@Override
	public String getImageUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(group, 0);
		

	}

	public static final Parcelable.Creator<GroupCapsule> CREATOR = new Parcelable.Creator<GroupCapsule>() {

		@Override
		public GroupCapsule createFromParcel(Parcel input) {
			// TODO Auto-generated method stub
			return new GroupCapsule(input);
		}

		@Override
		public GroupCapsule[] newArray(int size) {
			// TODO Auto-generated method stub
			return new GroupCapsule[size];
		}

	};

}
