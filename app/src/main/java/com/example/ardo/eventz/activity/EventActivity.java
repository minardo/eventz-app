package com.example.ardo.eventz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.adapter.EventAdapter;
import com.example.ardo.eventz.model.EventModel;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    RecyclerView rvEvent;
    ProgressDialog loading;

    Context mContext;
    List<EventModelResult> allEventItemList = new ArrayList<>();
    EventAdapter eventAdapter;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ardo.eventz.R.layout.activity_event);

        getSupportActionBar().setTitle("Event");

        rvEvent = (RecyclerView) findViewById(R.id.rvEvent);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        eventAdapter = new EventAdapter(this, allEventItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEvent.setLayoutManager(mLayoutManager);
        rvEvent.setItemAnimator(new DefaultItemAnimator());

        getResultListEvent();
    }

    private void getResultListEvent() {
        loading = ProgressDialog.show(this, null, "Please Wait...", true, false);

        mApiService.getAllEvent().enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();

                    final List<EventModelResult> results = response.body().getResults();

                    rvEvent.setAdapter(new EventAdapter(mContext, results));
                    eventAdapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Failed Pull Event Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
