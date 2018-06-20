package com.example.ardo.eventz.networking;

import android.content.Context;

import com.example.ardo.eventz.utils.AddCookiesInterceptor;
import com.example.ardo.eventz.utils.ReceivedCookiesInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Context context;

    public RetrofitClient(Context context) {
        this.context = context;
//        RetrofitClient retrofitClient = new RetrofitClient(this);
    }

    public static Retrofit getClient(String baseUrl) {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();

//        // Interceptor request
//        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
//        okhttpBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//
//                Request request = chain.request();
//
//                Request.Builder newRequest = request.newBuilder().addHeader("Cookie", "secret-key");
//
//                return chain.proceed(newRequest.build());
//            }
//        });

//        // Logging Interceptor
//        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        okHttpClientBuilder.addInterceptor(logging);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = null;
        okHttpClient = builder.addInterceptor(new AddCookiesInterceptor(context))
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
//                    .client(okHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}
