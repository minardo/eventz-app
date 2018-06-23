package com.example.ardo.eventz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEvent);
        loading = ProgressDialog.show(this, null, "Please Wait...", true, false);

        mApiService.getAllEvent().enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                final List<EventModelResult> results = response.body().getResults();
                if (response.isSuccessful()) {
                    loading.dismiss();

//                    final List<EventModelResult> results = response.body().getResults();

                    rvEvent.setAdapter(new EventAdapter(mContext, results));
                    eventAdapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Failed Pull Event Data", Toast.LENGTH_SHORT).show();
                }

                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);
//                            Toast.makeText(getApplicationContext(), "Id : " + results.get(position).getId() + " selected", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(EventActivity.this, EventDetailActivity.class);
                            i.putExtra("id", results.get(position).getId());
                            i.putExtra("name", results.get(position).getName());
                            i.putExtra("head_description", results.get(position).getDescription());
                            i.putExtra("description", results.get(position).getDescription());
                            i.putExtra("place", results.get(position).getPlace());
                            i.putExtra("contact", results.get(position).getContact());
                            i.putExtra("quota", results.get(position).getQuota().toString());
                            i.putExtra("datetime", results.get(position).getTime());
                            i.putExtra("category", results.get(position).getEventType());
                            EventActivity.this.startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
