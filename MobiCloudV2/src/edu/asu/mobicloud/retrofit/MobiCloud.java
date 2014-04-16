package edu.asu.mobicloud.retrofit;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import edu.asu.mobicloud.rest.model.ApplicationsList;
import edu.asu.mobicloud.rest.model.Group;
import edu.asu.mobicloud.rest.model.GroupsList;
import edu.asu.mobicloud.rest.model.Notification;
import edu.asu.mobicloud.rest.model.Result;
import edu.asu.mobicloud.rest.model.UserCapusule;
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
	void getApplications(@Header("token") String token,
			Callback<ApplicationsList> callback);

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
			Callback<Map<String, Group>> callback);

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

	@POST("/user_friends/add_friend/{userId}.json")
	void addFriend(@Header("token") String token, @Path("userId") String id,
			Callback<Map<String, String>> callback);

	@POST("/user_friends/unfriend/{userId}.json")
	void unFriend(@Header("token") String token, @Path("userId") String id,
			Callback<Map<String, String>> callback);

	@GET("/groups/index/{userId}.json")
	void getUsersGroups(@Header("token") String token,
			@Path("userId") String userId, Callback<GroupsList> callback);

	@GET("/user_friends/index/{userId}.json")
	void getUsersFriendsList(@Header("token") String token,
			@Path("userId") String userId, Callback<UsersList> callback);

	@GET("/user_followers/index/{userId}.json")
	void getFollowersUsersList(@Header("token") String token,
			@Path("userId") String userId, Callback<UsersList> callback);

	@GET("/applications/index/{userId}.json")
	void getUserApplications(@Header("token") String token,
			@Path("userId") String developerId,
			Callback<ApplicationsList> callback);

	@POST("/user_followers/follow/{userId}.json")
	void followDeveloper(@Header("token") String token,
			@Path("userId") String id, Callback<Map<String, String>> callback);

	@POST("/user_followers/unfollow/{userId}.json")
	void unFollowDeveloper(@Header("token") String token,
			@Path("userId") String id, Callback<Map<String, String>> callback);

	@GET("/users/index/{userId}.json")
	void getUser(@Header("token") String token, @Path("userId") String id,
			Callback<Map<String, UserCapusule>> callback);

	@FormUrlEncoded
	@POST("/user_friends/confirm_friend.json")
	void acceptFriend(@Header("token") String token,
			@Field("data[UserFriend][id]") String id,
			@Field("data[UserFriend][status]") String status,
			Callback<Map<String, String>> callback);

	@POST("/groups/group_unjoin/{groupId}.json")
	void unFollowGroup(@Header("token") String token,
			@Path("groupId") String id, Callback<Map<String, String>> callback);

	@GET("/search/search_applications/{searchString}.json")
	void searchApps(@Header("token") String token,
			@Path("searchString") String searchString,
			Callback<ApplicationsList> callback);

	@GET("/search/search_users/{searchString}.json")
	void searchUsers(@Header("token") String token,
			@Path("searchString") String searchString,
			Callback<UsersList> callback);

	@GET("/search/search_groups/{searchString}.json")
	void searchGroups(@Header("token") String token,
			@Path("searchString") String searchString,
			Callback<GroupsList> callback);

	@GET("/notifications.json")
	void getNotifications(@Header("token") String token,
			Callback<Map<String, List<Notification>>> callback);

}
