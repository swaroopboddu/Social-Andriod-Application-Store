package edu.asu.mobicloud.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import edu.asu.mobicloud.model.ListData;

public class ApplicationCapsule implements ListData, Parcelable {
	@SerializedName("Application")
	Application application;
	@SerializedName("User")
	User user;

	public ApplicationCapsule() {

	}

	public ApplicationCapsule(Parcel input) {
		application = input.readParcelable(Application.class.getClassLoader());
		user = input.readParcelable(User.class.getClassLoader());
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return application.title;
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
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeParcelable((Parcelable) application, 0);
		dest.writeParcelable((Parcelable) user, 0);

	}

	public static final Parcelable.Creator<ApplicationCapsule> CREATOR = new Parcelable.Creator<ApplicationCapsule>() {

		@Override
		public ApplicationCapsule createFromParcel(Parcel input) {
			// TODO Auto-generated method stub
			return new ApplicationCapsule(input);
		}

		@Override
		public ApplicationCapsule[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ApplicationCapsule[size];
		}

	};

}
