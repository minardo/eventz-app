package com.example.ardo.eventz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.adapter.MyEventAdapter;
import com.example.ardo.eventz.model.AllUserModel;
import com.example.ardo.eventz.model.AllUserModelResult;
import com.example.ardo.eventz.model.MyEventModel;
import com.example.ardo.eventz.model.MyEventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.RetrofitClient;
import com.example.ardo.eventz.networking.UtilsApi;
import com.example.ardo.eventz.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
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
    private static final String TAG = "MyEventActivity";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        getAllUser();
//        getClickListener();
    }

//    private void getClickListener() {
//        rvMyEvent.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//            });
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                if (child != null && gestureDetector.onTouchEvent(e)) {
//                    int position = rv.getChildAdapterPosition(child);
////                            Toast.makeText(getApplicationContext(), "Id : " + results.get(position).getId() + " selected", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(MyEventActivity.this, EventUpdateActivity.class);
//                    i.putExtra("id", userId);
//                    startActivity(i);
//
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//    }

    private void getAllUser() {
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
                                getResultListMyEvent();
                            }
                        }));
            }

            @Override
            public void onFailure(Call<AllUserModel> call, Throwable t) {

            }
        });
    }

    private void getResultListMyEvent() {
        loading = ProgressDialog.show(this, null, "Please Wait...", true, false);
        userId = Integer.valueOf(SessionManager.getCookiesPref(getApplicationContext(), SessionManager.SP_ID));
        Log.i(TAG, "UserID: "+userId);
        mApiService.getMyEvent(userId).enqueue(new Callback<MyEventModel>() {
            @Override
            public void onResponse(Call<MyEventModel> call, Response<MyEventModel> response) {
                if (response.body().getCount() == 0) {
                    Toast.makeText(mContext, "My Event Empty", Toast.LENGTH_SHORT).show();
                } else {
                    final List<MyEventModelResult> results = response.body().getResults();
                    for (int i=0; i<results.size(); i++) {
                        Log.i(TAG, "onResponse: "+results.get(i).getName());
                    }
                    if (response.isSuccessful()) {
                        myEventAdapter = new MyEventAdapter(MyEventActivity.this, results);
                        rvMyEvent.setAdapter(myEventAdapter);
                        myEventAdapter.notifyDataSetChanged();
                    } else {
                        loading.dismiss();
                        Toast.makeText(mContext, "Failed Get My Event Data", Toast.LENGTH_SHORT).show();
                    }
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<MyEventModel> call, Throwable t) {
                loading.dismiss();
//                Log.i(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
