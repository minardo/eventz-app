package com.example.ardo.eventz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.adapter.MyEventAdapter;
import com.example.ardo.eventz.model.MyEventModel;
import com.example.ardo.eventz.model.MyEventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.RetrofitClient;
import com.example.ardo.eventz.networking.UtilsApi;
import com.example.ardo.eventz.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ardo.eventz.utils.AddCookiesInterceptor.PREF_COOKIES;

public class MyEventActivity extends AppCompatActivity {

    RecyclerView rvMyEvent;
    ProgressDialog loading;

    Context mContext;
    List<MyEventModelResult> myEventItemList = new ArrayList<>();
    MyEventAdapter myEventAdapter;
    BaseApiService mApiService;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        getSupportActionBar().setTitle("My Event");

        rvMyEvent = (RecyclerView) findViewById(R.id.rvMyEvent);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        myEventAdapter = new MyEventAdapter(this, myEventItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMyEvent.setLayoutManager(mLayoutManager);
        rvMyEvent.setItemAnimator(new DefaultItemAnimator());
        rvMyEvent.setAdapter(myEventAdapter);

        getResultListMyEvent();
    }

    private void getResultListMyEvent() {
        loading = ProgressDialog.show(this, null, "Please Wait...", true, false);

        mApiService.getMyEvent(userId).enqueue(new Callback<MyEventModel>() {
            @Override
            public void onResponse(Call<MyEventModel> call, Response<MyEventModel> response) {
                final List<MyEventModelResult> results = response.body().getResults();
                if (response.isSuccessful()) {
                    loading.dismiss();

//                    rvMyEvent.setAdapter(new MyEventAdapter(mContext, results));
//                    myEventAdapter.notifyDataSetChanged();

                    myEventAdapter = new MyEventAdapter(MyEventActivity.this, results);
                    rvMyEvent.setAdapter(myEventAdapter);
                    myEventAdapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Failed Get My Event Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyEventModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "onFailure: ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
