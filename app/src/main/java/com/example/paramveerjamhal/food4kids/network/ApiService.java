package com.example.paramveerjamhal.food4kids.network;

import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.EventResponse;
import com.example.paramveerjamhal.food4kids.entities.PostResponse;
import com.example.paramveerjamhal.food4kids.entities.UserResponse;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.entities.Weekly_EventResponse;

import java.sql.Time;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name,
                               @Field("email") String email,
                               @Field("password") String password,
                               @Field("password_confirmation") String password_confirmation,
                               @Field("address") String address,
                               @Field("city") String city,
                               @Field("postal_code") String postal_code,
                               @Field("mobile") String mobile,
                               @Field("userType") int userType);

    @POST("update_user")
    @FormUrlEncoded
    Call<AccessToken> update_user(
                               @Field("name") String name,
                               @Field("email") String email,
                               @Field("address") String address,
                               @Field("city") String city,
                               @Field("postal_code") String postal_code,
                               @Field("mobile") String mobile);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username,
                            @Field("password") String password);


    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("posts")
    Call<PostResponse> posts();

    @GET("user")
    Call<UserResponse> user();

    @POST("events")
    @FormUrlEncoded
    Call<AccessToken> events(  @Field("userId" ) int userId,
                               @Field("eventType") int eventType,
                               @Field("eventTitle") String eventTitle,
                               @Field("eventDescription") String eventDescription,
                               @Field("eventAddress") String eventAddress,
                               @Field("postal_code") String postal_code,
                               @Field("event_Date") String event_Date,
                               @Field("event_Organizer") String event_Organizer,
                               @Field("date") String date,
                               @Field("start_time") String start_Timer,
                               @Field("end_time") String end_time);

    @GET("showevents")
    Call<EventResponse> showevents();

    @POST("delete_event")
    @FormUrlEncoded
    Call<AccessToken> delete_event(@Field("eventId") int eventId);

    @POST("update_event")
    @FormUrlEncoded
    Call<AccessToken> update_event(@Field("eventId") int eventId,
                                   @Field("userId" ) int userId,
                                   @Field("eventTitle") String eventTitle,
                                   @Field("eventDescription") String eventDescription,
                                   @Field("eventAddress") String eventAddress,
                                   @Field("postal_code") String postal_code,
                                   @Field("event_Date") String event_Date,
                                   @Field("event_Organizer") String event_Organizer);

    @GET("packing_calender")
    Call<Weekly_EventResponse> packing_calender();

    @GET("sorting_calender")
    Call<Weekly_EventResponse> sorting_calender();


    @POST("logout")
    Call<AccessToken> logout(@Field ("access_token") String accessToken);


}
//  @Field("address") String address, //   @Field("reEnterPassword") String reEnterPassword)  ; //  @Field("mobile") String mobile,