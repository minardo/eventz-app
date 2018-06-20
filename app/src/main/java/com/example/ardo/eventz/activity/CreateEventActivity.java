package com.example.ardo.eventz.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;
import com.example.ardo.eventz.model.CreateEventModel;
import com.example.ardo.eventz.utils.CustomDateTimePicker;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends AppCompatActivity {

    CustomDateTimePicker custom;
    DatePickerDialog.OnDateSetListener date;
    EditText editTextNameEvent, editTextDescription,
            editTextPlace, editTextContact, editTextQuota,
            editTextEventType, editTextDateTime;
    private TextView mResponseTv;
    Button buttonCreateEventActivity;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mBaseApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mContext = this;
        mBaseApiService = UtilsApi.getApiService();

        final EditText editTextNameEvent = findViewById(R.id.etNameEvent);
        final EditText editTextDescription = findViewById(R.id.etDescription);
        final EditText editTextPlace = findViewById(R.id.etPlace);
        final EditText  editTextContact = findViewById(R.id.etContact);
        final EditText editTextQuota = findViewById(R.id.etQuota);
        final EditText editTextDateTime = findViewById(R.id.etDateTime);
        final EditText editTextEventType = findViewById(R.id.etEventType);
        buttonCreateEventActivity = findViewById(R.id.btnCreateEventActivity);
        mResponseTv = findViewById(R.id.tvResponse);

        custom = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected,
                              int year, String monthFullName, String monthShortName, int monthNumber,
                              int date, String weekDayFullName, String weekDayShortName,
                              int hour24, int hour12, int min, int sec, String AM_PM) {
                editTextDateTime.setText("");
                editTextDateTime.setText(
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
        editTextDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom.showDialog();
            }
        });

        buttonCreateEventActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextNameEvent.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String place = editTextPlace.getText().toString().trim();
                String contact = editTextContact.getText().toString().trim();
                String quota = editTextQuota.getText().toString().trim();
                String time = editTextDateTime.getText().toString().trim();
                String event_type = editTextEventType.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description)
                        && !TextUtils.isEmpty(place) && !TextUtils.isEmpty(contact)
                        && !TextUtils.isEmpty(quota) && !TextUtils.isEmpty(time)
                        && !TextUtils.isEmpty(event_type)) {
                    createEvent(name, description, place, contact, quota, time, event_type);
                }
                progressDialog = ProgressDialog.show(mContext, null, "Please Wait...", true, false);
            }
        });
    }

    public void createEvent(String name, String description,
                            String place, String contact,
                            String quota, String time,
                            String event_type) {
        mBaseApiService.createEvent("", name, description,
                place, contact, quota, time, event_type).enqueue(new Callback<CreateEventModel>() {
            @Override
            public void onResponse(Call<CreateEventModel> call, Response<CreateEventModel> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i("debug", "post submitted to API." + response.body().toString());
                    Toast.makeText(mContext, "Create Event Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateEventModel> call, Throwable t) {
                Log.e("debug", "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(String response) {
        if (mResponseTv.getVisibility() == View.GONE) {
            mResponseTv.setVisibility(View.VISIBLE);
        }
        mResponseTv.setText(response);
    }
}
