package com.example.ardo.eventz.networking;

import com.example.ardo.eventz.model.AllUserModel;
import com.example.ardo.eventz.model.CreateEventModel;
import com.example.ardo.eventz.model.EventModel;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.model.JoinEventModel;
import com.example.ardo.eventz.model.MyEventModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("api_user/login/")
    Call<ResponseBody> loginRequest(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api_user/register/")
    Call<ResponseBody> registerRequest(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api_event/event/")
    Call<CreateEventModel> createEvent(
            @Header("csrftoken") String csrftoken,
            @Field("name") String name,
            @Field("description") String description,
            @Field("place") String place,
            @Field("contact") String contact,
            @Field("quota") String quota,
            @Field("time") String time,
            @Field("event_type") String event_type
    );

    @PUT("api_event/event/{id}")
    @FormUrlEncoded
    Call<CreateEventModel> updateEvent(
            @Path("id") int idEvent,
            @Field("name") String name,
            @Field("description") String description,
            @Field("place") String place,
            @Field("contact") String contact,
            @Field("quota") String quota,
            @Field("time") String time,
            @Field("event_type") String event_type
    );

    @GET("api_event/event/")
    Call<EventModel> getAllEvent();

    @GET("api_event/event/{id}")
    Call<EventModelResult> getItemEvent(
            @Path("id") Integer id
    );

    @GET("api_user/users/{id}/events/")
    Call<MyEventModel> getMyEvent(
            @Path("id") Integer userId
    );

    @GET("api_user/users")
    Call<AllUserModel> getAllUser();

    @GET("api_event/event/{id}/join/")
    Call<JoinEventModel> getJoinEvent(
            @Path("id") Integer id
    );
}