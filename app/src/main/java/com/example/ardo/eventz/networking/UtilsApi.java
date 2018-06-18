package com.example.ardo.eventz.networking;

public class UtilsApi {
    public static final String BASE_URL_API = "https://eventzx.herokuapp.com/";

    public static BaseApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
