package com.example.ardo.eventz.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.model.CreateEventModel;
import com.example.ardo.eventz.model.EventModel;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventUpdateActivity extends AppCompatActivity {

    private static final String TAG = "EventUpdateActivity";
    BaseApiService mBaseApiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int id,idEvent;
    private EditText etUpdateNameEvent,etUpdateDescription,etUpdatePlace,etUpdateContact,etUpdateQuota,etUpdateDateTime,etUpdateEventType;
    private Button btnUpdateEventActivity;
    private String nameEvent,desc,place,contact,quota,time,eventType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

        mBaseApiService = UtilsApi.getApiService();
        etUpdateNameEvent = findViewById(R.id.etUpdateNameEvent);
        etUpdateDescription = findViewById(R.id.etUpdateDescription);
        etUpdatePlace = findViewById(R.id.etUpdatePlace);
        etUpdateContact = findViewById(R.id.etUpdateContact);
        etUpdateQuota = findViewById(R.id.etUpdateQuota);
        etUpdateDateTime = findViewById(R.id.etUpdateDateTime);
        etUpdateEventType = findViewById(R.id.etUpdateEventType);
        btnUpdateEventActivity = findViewById(R.id.btnUpdateEventActivity);

        btnUpdateEventActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesUpdate();
            }
        });

        id = getIntent().getIntExtra("id", 0);
        getData();
    }

    private void prosesUpdate() {
        nameEvent = etUpdateNameEvent.getText().toString().trim();
        desc = etUpdateDescription.getText().toString().trim();
        place = etUpdatePlace.getText().toString().trim();
        contact = etUpdateContact.getText().toString().trim();
        quota = etUpdateQuota.getText().toString().trim();
        time = etUpdateDateTime.getText().toString().trim();
        eventType = etUpdateEventType.getText().toString().trim();

        mBaseApiService.updateEvent(idEvent,nameEvent,desc,place,contact,quota,time,eventType)
                .enqueue(new Callback<CreateEventModel>() {
                    @Override
                    public void onResponse(Call<CreateEventModel> call, Response<CreateEventModel> response) {
                        Log.i(TAG, "onResponse: "+response.code());
                        Log.i(TAG, "Ini Quota ya: "+response.body().getName());
                        Log.i(TAG, "onResponse: "+idEvent);
                    }

                    @Override
                    public void onFailure(Call<CreateEventModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private void getData() {
        mBaseApiService.getAllEvent().enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                Log.i(TAG, "Ini Id: "+id);
                compositeDisposable.add(Observable
                        .fromIterable(response.body().getResults())
                        .filter(new Predicate<EventModelResult>() {
                            @Override
                            public boolean test(EventModelResult eventModelResult) {
                                return eventModelResult.getUserId().equals(id);
                            }
                        })
                        .subscribe(new Consumer<EventModelResult>() {
                            @Override
                            public void accept(EventModelResult eventModelResult) {
                                Log.i(TAG, "accept: "+eventModelResult.getName());
                                etUpdateNameEvent.setText(eventModelResult.getName());
                                etUpdateDescription.setText(eventModelResult.getDescription());
                                etUpdateContact.setText(eventModelResult.getContact());
                                etUpdateDateTime.setText(eventModelResult.getTime());
                                etUpdateEventType.setText(eventModelResult.getEventType());
                                etUpdatePlace.setText(eventModelResult.getPlace());
                                etUpdateQuota.setText(eventModelResult.getQuota().toString());
                                idEvent = eventModelResult.getId();
                            }
                        }));

            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
