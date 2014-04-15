package edu.asu.mobicloud.retrofit;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import edu.asu.mobicloud.rest.model.ApplicationsList;
import edu.asu.mobicloud.rest.model.GroupsList;
import edu.asu.mobicloud.rest.model.Result;
import edu.asu.mobicloud.rest.model.UsersList;

interface MobiCloud {
	@FormUrlEncoded
	@POST("/users/mobile_login.json")
	Result tryLogin(@Field("data[User][email]") String email,
			@Field("data[User][password]") String password);

	@FormUrlEncoded
	@POST("/users/add.json")
	Result register(@Field("data[User][first_name]") String firstName,
			@Field("data[User][last_name]") String lastName,
			@Field("data[User][email]") String email,
			@Field("data[User][password]") String password,
			@Field("data[User][password_confirmation]") String confirmation,
			@Field("data[User][phone]") String phone);

	@GET("/applications.json")
	ApplicationsList getApplications(@Header("token") String token);

	@GET("/groups.json")
	void getGroups(@Header("token") String token, Callback<GroupsList> callback);

	@GET("/friends.json")
	ApplicationsList getFriends(@Header("token") String token);

	@GET("/search.json")
	ApplicationsList search(@Header("token") String token,
			@Field("data[search]") String search);

	@FormUrlEncoded
	@POST("/groups/add.json")
	void createGroup(@Header("token") String token,
			@Field("data[Group][name]") String name,
			@Field("data[Group][description]") String description,
			Callback<GroupsList> callback);

	@GET("/applications.json")
	void getApps(Callback<ApplicationsList> callback);

	@GET("/groups/view/{groupId}.json")
	void getMembers(@Header("token") String token,
			@Path("groupId") String groupId, Callback<UsersList> callback);

	@POST("/groups/group_join/{groupId}.json")
	void followGroup(@Header("token") String token, @Path("groupId") String id,
			Callback<Map<String, String>> callback);

	@GET("/groups.json")
	void getGroups(Callback<GroupsList> callback);

	@GET("/users/mobile_users.json")
	void getPublicUsersList(@Header("token") String token,
			Callback<UsersList> callback);

	@GET("/user_friends.json")
	void getFriendsList(@Header("token") String token,
			Callback<UsersList> callback);

	@GET("/user_followers.json")
	void getFollowersList(@Header("token") String token,
			Callback<UsersList> callback);

	@GET("/users/developers.json")
	void getFollowersPublicList(@Header("token") String token,
			Callback<UsersList> callback);

}
