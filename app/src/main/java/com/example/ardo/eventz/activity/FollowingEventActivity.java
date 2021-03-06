package com.example.ardo.eventz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.adapter.FollowingEventAdapter;
import com.example.ardo.eventz.adapter.MyEventAdapter;
import com.example.ardo.eventz.model.AllUserModel;
import com.example.ardo.eventz.model.AllUserModelResult;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.model.FollowingEventModel;
import com.example.ardo.eventz.model.FollowingEventModelResult;
import com.example.ardo.eventz.model.MyEventModel;
import com.example.ardo.eventz.model.MyEventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;
import com.example.ardo.eventz.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingEventActivity extends AppCompatActivity {

    RecyclerView rvFollowingEvent;
    ProgressDialog loading;

    Context mContext;
    List<FollowingEventModelResult> followingEventItemList = new ArrayList<>();
    FollowingEventAdapter followingEventAdapter;
    BaseApiService mApiService;
    Integer userId;
    private static final String TAG = "FollowingEventActivity";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_event);

        getSupportActionBar().setTitle("Following Event");

        rvFollowingEvent = (RecyclerView) findViewById(R.id.rvFollowingEvent);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        followingEventAdapter = new FollowingEventAdapter(this, followingEventItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvFollowingEvent.setLayoutManager(mLayoutManager);
        rvFollowingEvent.setItemAnimator(new DefaultItemAnimator());
        rvFollowingEvent.setAdapter(followingEventAdapter);

        getAllUser();
    }

    public void getAllUser() {
        final String username = SessionManager.getCookiesPref(getApplicationContext(), SessionManager.SP_USERNAME);
        Log.i(TAG, "getAllUser: "+username);
        mApiService.getAllUser().enqueue(new Callback<AllUserModel>() {
            @Override
            public void onResponse(Call<AllUserModel> call, Response<AllUserModel> response) {
                compositeDisposable.add(Observable
                        .fromIterable(response.body().getResults())
                        .filter(new Predicate<AllUserModelResult>() {
                            @Override
                            public boolean test(AllUserModelResult resultAllUser) {
                                return resultAllUser.getUsername().equals(username);
                            }
                        })
                        .subscribe(new Consumer<AllUserModelResult>() {
                            @Override
                            public void accept(AllUserModelResult resultAllUser) {
                                String idUser = resultAllUser.getId().toString();
                                SessionManager.saveSPString(getApplicationContext(), SessionManager.SP_ID,idUser);
                                getResultListFollowingEvent();
                            }
                        }));
            }

            @Override
            public void onFailure(Call<AllUserModel> call, Throwable t) {

            }
        });
    }

    private void getResultListFollowingEvent() {
        final RecyclerView recyclerView = findViewById(R.id.rvFollowingEvent);
        loading = ProgressDialog.show(this, null, "Please Wait...", true, false);
        userId = Integer.valueOf(SessionManager.getCookiesPref(getApplicationContext(), SessionManager.SP_ID));
        Log.i(TAG, "UserID: "+userId);
        mApiService.getFollowingEvent(userId).enqueue(new Callback<FollowingEventModel>() {
            @Override
            public void onResponse(Call<FollowingEventModel> call, Response<FollowingEventModel> response) {
                final List<FollowingEventModelResult> results = response.body().getResults();
//                if (0 == response.body().getCount()) {
//                    Toast.makeText(mContext, "Following Event Empty", Toast.LENGTH_SHORT).show();
//                } else  {
//                    final List<FollowingEventModelResult> results = response.body().getResults();
//                    for (int i=0; i<results.size(); i++) {
//                        Log.i(TAG, "onResponse: "+results.get(i).getName());
//                    }
//                    if (response.isSuccessful()) {
//                        followingEventAdapter = new FollowingEventAdapter(FollowingEventActivity.this, results);
//                        rvFollowingEvent.setAdapter(followingEventAdapter);
//                        followingEventAdapter.notifyDataSetChanged();
//                    } else {
//                        loading.dismiss();
//                        Toast.makeText(mContext, "Failed Get Following Event Data", Toast.LENGTH_SHORT).show();
//                    }
//                }
                if (Objects.requireNonNull(response.body()).getCount() == 0) {
                    Toast.makeText(mContext, "Following Event Empty", Toast.LENGTH_SHORT).show();
                } else  {
//                    final List<FollowingEventModelResult> results = response.body().getResults();
                    for (int i=0; i<results.size(); i++) {
                        Log.i(TAG, "onResponse: "+results.get(i).getName());
                    }
                    if (response.isSuccessful()) {
                        followingEventAdapter = new FollowingEventAdapter(FollowingEventActivity.this, results);
                        rvFollowingEvent.setAdapter(followingEventAdapter);
                        followingEventAdapter.notifyDataSetChanged();
                    } else {
                        loading.dismiss();
                        Toast.makeText(mContext, "Failed Get Following Event Data", Toast.LENGTH_SHORT).show();
                    }
                }
                loading.dismiss();

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

                            Intent i = new Intent(FollowingEventActivity.this, FollowingEventDetailActivity.class);
                            i.putExtra("id", results.get(position).getId());
                            i.putExtra("name", results.get(position).getName());
                            i.putExtra("head_description", results.get(position).getDescription());
                            i.putExtra("description", results.get(position).getDescription());
                            i.putExtra("place", results.get(position).getPlace());
                            i.putExtra("contact", results.get(position).getContact());
                            i.putExtra("quota", results.get(position).getQuota().toString());
                            i.putExtra("datetime", results.get(position).getTime());
                            i.putExtra("category", results.get(position).getEventType());
                            FollowingEventActivity.this.startActivity(i);
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
            public void onFailure(Call<FollowingEventModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
