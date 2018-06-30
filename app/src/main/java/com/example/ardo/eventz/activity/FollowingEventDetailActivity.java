package com.example.ardo.eventz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.model.JoinEventModel;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingEventDetailActivity extends AppCompatActivity {

    public TextView tvNameEventDetail, tvDescEventDetail, tvPlaceEventDetail,
            tvContactPersonEventDetail, tvQuotaEventDetail, tvTimeEventDetail,
            tvTypeEventDetail, tvHeadDescEventDetail, tvHeadPlaceEventDetail,
            tvHeadContactPersonEventDetail, tvHeadQuotaEventDetail, tvHeadTimeEventDetail,
            tvHeadTypeEventDetail;
    public ImageView bgNameEventDetail;
    public Button btnEventRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_event_detail);

        tvNameEventDetail = (TextView) findViewById(R.id.tvNameFollowingEventDetail);
        tvNameEventDetail.setText(getIntent().getStringExtra("name"));

        tvHeadDescEventDetail = (TextView) findViewById(R.id.tvHeadDescFollowingEventDetail);
        tvHeadDescEventDetail.setText("Description");
        tvDescEventDetail = (TextView) findViewById(R.id.tvDescFollowingEventDetail);
        tvDescEventDetail.setText(getIntent().getStringExtra("description"));

        tvHeadPlaceEventDetail= (TextView) findViewById(R.id.tvHeadPlaceFollowingEventDetail);
        tvHeadPlaceEventDetail.setText("Place");
        tvPlaceEventDetail = (TextView) findViewById(R.id.tvPlaceFollowingEventDetail);
        tvPlaceEventDetail.setText(getIntent().getStringExtra("place"));

        tvHeadContactPersonEventDetail = (TextView) findViewById(R.id.tvHeadContactPersonFollowingEventDetail);
        tvHeadContactPersonEventDetail.setText("Contact");
        tvContactPersonEventDetail = (TextView) findViewById(R.id.tvContactPersonFollowingEventDetail);
        tvContactPersonEventDetail.setText(getIntent().getStringExtra("contact"));

        tvHeadQuotaEventDetail = (TextView) findViewById(R.id.tvHeadQuotaFollowingEventDetail);
        tvHeadQuotaEventDetail.setText("Quota");
        tvQuotaEventDetail = (TextView) findViewById(R.id.tvQuotaFollowingEventDetail);
        tvQuotaEventDetail.setText(getIntent().getStringExtra("quota"));

        tvHeadTimeEventDetail = (TextView) findViewById(R.id.tvHeadTimeFollowingEventDetail);
        tvHeadTimeEventDetail.setText("Date Time");
        tvTimeEventDetail = (TextView) findViewById(R.id.tvTimeFollowingEventDetail);
        tvTimeEventDetail.setText(getIntent().getStringExtra("datetime"));

        tvHeadTypeEventDetail = (TextView) findViewById(R.id.tvHeadTypeFollowingEventDetail);
        tvHeadTypeEventDetail.setText("Category");
        tvTypeEventDetail = (TextView) findViewById(R.id.tvTypeFollowingEventDetail);
        tvTypeEventDetail.setText(getIntent().getStringExtra("category"));
    }
}
