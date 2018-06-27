package com.example.ardo.eventz.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.model.HaveBeenJoined;
import com.example.ardo.eventz.model.JoinEventModel;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends AppCompatActivity {

    public TextView tvNameEventDetail, tvDescEventDetail, tvPlaceEventDetail,
            tvContactPersonEventDetail, tvQuotaEventDetail, tvTimeEventDetail,
            tvTypeEventDetail, tvHeadDescEventDetail, tvHeadPlaceEventDetail,
            tvHeadContactPersonEventDetail, tvHeadQuotaEventDetail, tvHeadTimeEventDetail,
            tvHeadTypeEventDetail;
    public ImageView bgNameEventDetail;
    public Button btnEventRegister;
    private Integer quota, id;
    Intent intent = getIntent();
    private static final String TAG = "EventDetailActivity";
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        id = getIntent().getIntExtra("id", 0);
        tvNameEventDetail = (TextView) findViewById(R.id.tvNameEventDetail);

        tvHeadDescEventDetail = (TextView) findViewById(R.id.tvHeadDescEventDetail);
        tvHeadDescEventDetail.setText("Description");
        tvDescEventDetail = (TextView) findViewById(R.id.tvDescEventDetail);

        tvHeadPlaceEventDetail= (TextView) findViewById(R.id.tvHeadPlaceEventDetail);
        tvHeadPlaceEventDetail.setText("Place");
        tvPlaceEventDetail = (TextView) findViewById(R.id.tvPlaceEventDetail);

        tvHeadContactPersonEventDetail = (TextView) findViewById(R.id.tvHeadContactPersonEventDetail);
        tvHeadContactPersonEventDetail.setText("Contact");
        tvContactPersonEventDetail = (TextView) findViewById(R.id.tvContactPersonEventDetail);

        tvHeadQuotaEventDetail = (TextView) findViewById(R.id.tvHeadQuotaEventDetail);
        tvHeadQuotaEventDetail.setText("Quota");
        tvQuotaEventDetail = (TextView) findViewById(R.id.tvQuotaEventDetail);

        tvHeadTimeEventDetail = (TextView) findViewById(R.id.tvHeadTimeEventDetail);
        tvHeadTimeEventDetail.setText("Date Time");
        tvTimeEventDetail = (TextView) findViewById(R.id.tvTimeEventDetail);

        tvHeadTypeEventDetail = (TextView) findViewById(R.id.tvHeadTypeEventDetail);
        tvHeadTypeEventDetail.setText("Category");
        tvTypeEventDetail = (TextView) findViewById(R.id.tvTypeEventDetail);

        btnEventRegister = (Button) findViewById(R.id.btnEventRegister);

        btnEventRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEvent();
            }
        });

        getData();

//        afterJoin();
    }

//    private void afterJoin() {
//        Log.i(TAG, "afterJoin: ");
//        mApiService = UtilsApi.getApiService();
//        mApiService.getJoinEvent(id).enqueue(new Callback<JoinEventModel>() {
//            @Override
//            public void onResponse(Call<JoinEventModel> call, Response<JoinEventModel> response) {
//                if (response.code() == 406){
//                    btnEventRegister.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JoinEventModel> call, Throwable t) {
//                Log.i(TAG, "onFailure: Join Event Failure");
//                Toast.makeText(EventDetailActivity.this, "Join Event Failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void joinEvent() {
        mApiService = UtilsApi.getApiService();
        mApiService.getJoinEvent(id).enqueue(new Callback<JoinEventModel>() {
            @Override
            public void onResponse(Call<JoinEventModel> call, Response<JoinEventModel> response) {
                if (response.isSuccessful()) {
                    String statusOk = response.body().getStatus();
                    Log.i(TAG, "onResponse: Join Event Success"+statusOk +response);
                    Toast.makeText(EventDetailActivity.this, "Join Event Success", Toast.LENGTH_SHORT).show();
                    if (statusOk.equals("ok")) {
                        Log.i(TAG, "onResponse: TEST BRO");
                        btnEventRegister.setVisibility(View.INVISIBLE);
                    }
                } else {
                    switch (response.code()) {
                        case 406:
                            Log.i(TAG, "onResponse: You have been joined" +response);
                            Toast.makeText(EventDetailActivity.this, "You have been joined", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Log.i(TAG, "onResponse: Event has passed" +response);
                            Toast.makeText(EventDetailActivity.this, "Event has passed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinEventModel> call, Throwable t) {
                Log.i(TAG, "onFailure: Join Event Failure");
                Toast.makeText(EventDetailActivity.this, "Join Event Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getData() {
        mApiService = UtilsApi.getApiService();
        mApiService.getItemEvent(id).enqueue(new Callback<EventModelResult>() {
            @Override
            public void onResponse(Call<EventModelResult> call, Response<EventModelResult> response) {
                tvNameEventDetail.setText(response.body().getName());
                tvDescEventDetail.setText(response.body().getDescription());
                tvPlaceEventDetail.setText(response.body().getPlace());
                tvContactPersonEventDetail.setText(response.body().getContact());
                tvQuotaEventDetail.setText(response.body().getQuota().toString());
                tvTimeEventDetail.setText(response.body().getTime());
                tvTypeEventDetail.setText(response.body().getEventType());
            }

            @Override
            public void onFailure(Call<EventModelResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " +t.getMessage());
            }
        });
    }
}
