package edu.asu.mobicloud.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Application implements Parcelable {
	@SerializedName("id")
	String id;
	@SerializedName("user_id")
	String userId;
	@SerializedName("title")
	String title;
	@SerializedName("description")
	String description;
	@SerializedName("path")
	String path;
	@SerializedName("count_rating")
	String ratingCount;
	@SerializedName("rating")
	String rating;

	public Application() {

	}

	public Application(Parcel input) {
		id = input.readString();
		userId = input.readString();
		title = input.readString();
		description = input.readString();
		path = input.readString();
		ratingCount = input.readString();
		rating = input.readString();
	}

	@Override
	public String toString() {
		return "Application [title=" + title + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(String ratingCount) {
		this.ratingCount = ratingCount;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public static final Parcelable.Creator<Application> CREATOR = new Parcelable.Creator<Application>() {

		@Override
		public Application createFromParcel(Parcel input) {
			// TODO Auto-generated method stub
			return new Application(input);
		}

		@Override
		public Application[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Application[size];
		}

	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(userId);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(path);
		dest.writeString(ratingCount);
		dest.writeString(rating);

	}

}
