package com.example.ardo.eventz.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.model.CreateEventModel;
import com.example.ardo.eventz.model.EventModel;
import com.example.ardo.eventz.model.EventModelResult;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;
import com.example.ardo.eventz.utils.CustomDateTimePicker;
import com.example.ardo.eventz.utils.SessionManager;

import java.util.Calendar;
import java.util.Date;

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
    private int userID,idEvent;
    private EditText etUpdateNameEvent,etUpdateDescription,etUpdatePlace,etUpdateContact,etUpdateQuota,etUpdateDateTime,etUpdateEventType;
    private Button btnUpdateEventActivity;
    private String nameEvent,desc,place,contact,quota,time,eventType;
    ProgressDialog loading;
    CustomDateTimePicker custom;

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

        custom = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected,
                              int year, String monthFullName, String monthShortName, int monthNumber,
                              int date, String weekDayFullName, String weekDayShortName,
                              int hour24, int hour12, int min, int sec, String AM_PM) {
                etUpdateDateTime.setText("");
                etUpdateDateTime.setText(
                        year + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min + ":" + "00+00:00");
            }

            @Override
            public void onCancel() {

            }
        });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
        custom.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom.setDate(Calendar.getInstance());
        etUpdateDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom.showDialog();
            }
        });

        btnUpdateEventActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesUpdate();
            }
        });

        idEvent = getIntent().getIntExtra("id", 0);
        nameEvent= getIntent().getStringExtra("name");
        desc= getIntent().getStringExtra("description");
        place= getIntent().getStringExtra("place");
        contact= getIntent().getStringExtra("contact");
        time= getIntent().getStringExtra("time");
        eventType= getIntent().getStringExtra("event_type");
        quota = String.valueOf(getIntent().getIntExtra("quota", 0));


        etUpdateNameEvent.setText(nameEvent);
        etUpdateDescription.setText(desc);
        etUpdateQuota.setText(quota);
        etUpdatePlace.setText(place);
        etUpdateDateTime.setText(time);
        etUpdateContact.setText(contact);
        etUpdateEventType.setText(eventType);

//        getData();
    }

    private void prosesUpdate() {
        nameEvent = etUpdateNameEvent.getText().toString().trim();
        desc = etUpdateDescription.getText().toString().trim();
        place = etUpdatePlace.getText().toString().trim();
        contact = etUpdateContact.getText().toString().trim();
        quota = etUpdateQuota.getText().toString().trim();
        time = etUpdateDateTime.getText().toString().trim();
        eventType = etUpdateEventType.getText().toString().trim();

//        System.out.println("id : "idEvent);
        loading = ProgressDialog.show(EventUpdateActivity.this, null, "Please Wait...", true, false);
        mBaseApiService.updateEvent(idEvent,nameEvent,desc,place,contact,quota,time,eventType)
                .enqueue(new Callback<CreateEventModel>() {
                    @Override
                    public void onResponse(Call<CreateEventModel> call, Response<CreateEventModel> response) {
                        Log.i(TAG, "onResponse: "+response.code());
                        Log.i(TAG, "Ini Quota ya: "+response.body().getName());
                        Log.i(TAG, "onResponse: "+idEvent);
                        loading.dismiss();
                        Intent intent = new Intent(EventUpdateActivity.this, MyEventActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<CreateEventModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage());
                        loading.dismiss();
                    }
                });
    }

    private void getData() {
        mBaseApiService.getAllEvent().enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                Log.i(TAG, "Ini user Id: "+userID);
                compositeDisposable.add(Observable
                        .fromIterable(response.body().getResults())
                        .filter(new Predicate<EventModelResult>() {
                            @Override
                            public boolean test(EventModelResult eventModelResult) {
                                return eventModelResult.getUserId().equals(userID);
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


    private void prosesDelete() {
        mBaseApiService.deleteMyEvent(idEvent).enqueue(new Callback<EventModelResult>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(Call<EventModelResult> call, Response<EventModelResult> response) {
                Toast.makeText(getApplicationContext(), "Delete Event Success ", Toast.LENGTH_LONG);
                Intent intent = new Intent(EventUpdateActivity.this, MyEventActivity.class);
                startActivity(intent);
            }

            @SuppressLint("ShowToast")
            @Override
            public void onFailure(Call<EventModelResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Delete Event Failed ", Toast.LENGTH_LONG);
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_delete:
                prosesDelete();
                return true;
            default :
                return  false;
        }

    }
}
