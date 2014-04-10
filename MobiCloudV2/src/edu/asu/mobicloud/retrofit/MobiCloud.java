package edu.asu.mobicloud.retrofit;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import edu.asu.mobicloud.rest.model.ApplicationsList;
import edu.asu.mobicloud.rest.model.Result;

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
}
