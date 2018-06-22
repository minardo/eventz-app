package com.example.ardo.eventz.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ardo.eventz.R;

public class EventDetailActivity extends AppCompatActivity {

    public TextView tvNameEventDetail, tvDescEventDetail, tvPlaceEventDetail,
            tvContactPersonEventDetail, tvQuotaEventDetail, tvTimeEventDetail,
            tvTypeEventDetail, tvHeadDescEventDetail, tvHeadPlaceEventDetail,
            tvHeadContactPersonEventDetail, tvHeadQuotaEventDetail, tvHeadTimeEventDetail,
            tvHeadTypeEventDetail;
    public ImageView bgNameEventDetail;
    private Integer quota;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        tvNameEventDetail = (TextView) findViewById(R.id.tvNameEventDetail);
        tvNameEventDetail.setText(getIntent().getStringExtra("name"));

        tvHeadDescEventDetail = (TextView) findViewById(R.id.tvHeadDescEventDetail);
        tvHeadDescEventDetail.setText("Description");
        tvDescEventDetail = (TextView) findViewById(R.id.tvDescEventDetail);
        tvDescEventDetail.setText(getIntent().getStringExtra("description"));

        tvHeadPlaceEventDetail= (TextView) findViewById(R.id.tvHeadPlaceEventDetail);
        tvHeadPlaceEventDetail.setText("Place");
        tvPlaceEventDetail = (TextView) findViewById(R.id.tvPlaceEventDetail);
        tvPlaceEventDetail.setText(getIntent().getStringExtra("place"));

        tvHeadContactPersonEventDetail = (TextView) findViewById(R.id.tvHeadContactPersonEventDetail);
        tvHeadContactPersonEventDetail.setText("Contact");
        tvContactPersonEventDetail = (TextView) findViewById(R.id.tvContactPersonEventDetail);
        tvContactPersonEventDetail.setText(getIntent().getStringExtra("contact"));

        tvHeadQuotaEventDetail = (TextView) findViewById(R.id.tvHeadQuotaEventDetail);
        tvHeadQuotaEventDetail.setText("Quota");
        tvQuotaEventDetail = (TextView) findViewById(R.id.tvQuotaEventDetail);
        tvQuotaEventDetail.setText(getIntent().getStringExtra("quota"));

        tvHeadTimeEventDetail = (TextView) findViewById(R.id.tvHeadTimeEventDetail);
        tvHeadTimeEventDetail.setText("Date Time");
        tvTimeEventDetail = (TextView) findViewById(R.id.tvTimeEventDetail);
        tvTimeEventDetail.setText(getIntent().getStringExtra("datetime"));

        tvHeadTypeEventDetail = (TextView) findViewById(R.id.tvHeadTypeEventDetail);
        tvHeadTypeEventDetail.setText("Category");
        tvTypeEventDetail = (TextView) findViewById(R.id.tvTypeEventDetail);
        tvTypeEventDetail.setText(getIntent().getStringExtra("category"));
    }
}
