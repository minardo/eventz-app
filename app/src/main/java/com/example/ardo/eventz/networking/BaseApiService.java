package com.example.ardo.eventz.networking;

import com.example.ardo.eventz.model.CreateEventModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("api_user/login/")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("api_user/register/")
    Call<ResponseBody> registerRequest(@Field("username") String username,
                                       @Field("first_name") String first_name,
                                       @Field("last_name") String last_name,
                                       @Field("email") String email,
                                       @Field("password") String password);

    @FormUrlEncoded
    @POST("api_event/event/")
    Call<CreateEventModel> createEvent(@Header("csrftoken") String csrftoken,
                                       @Field("name") String name,
                                       @Field("description") String description,
                                       @Field("place") String place,
                                       @Field("contact") String contact,
                                       @Field("quota") String quota,
                                       @Field("time") String time,
                                       @Field("event_type") String event_type);
}