package edu.asu.mobicloud.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
	@SerializedName("id	")
	String id;
	@SerializedName("first_name")
	String firstName;
	@SerializedName("last_name")
	String lastName;
	@SerializedName("email")
	String email;
	@SerializedName("token")
	String token;
	@SerializedName("role")
	String role;

	public User(Parcel input) {
		id = input.readString();
		firstName = input.readString();
		lastName = input.readString();
		email = input.readString();
		token = input.readString();
		role = input.readString();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(email);
		dest.writeString(token);
		dest.writeString(role);

	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel input) {
			// TODO Auto-generated method stub
			return new User(input);
		}

		@Override
		public User[] newArray(int size) {
			// TODO Auto-generated method stub
			return new User[size];
		}

	};

}
