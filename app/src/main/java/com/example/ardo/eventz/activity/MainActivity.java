package com.example.ardo.eventz.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ardo.eventz.activity.EventActivity;
import com.example.ardo.eventz.R;
import com.example.ardo.eventz.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    TextView tvUsername;
    Button buttonEvent, buttonEditProfile, buttonMyEvent, buttonCreateEvent, buttonFollowingEvent, buttonLogout;
    SessionManager sessionManager;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        buttonEvent = (Button) findViewById(R.id.btnEvent);
        buttonMyEvent = (Button) findViewById(R.id.btnMyEvent);
        buttonCreateEvent = (Button) findViewById(R.id.btnCreateEvent);
        buttonFollowingEvent = (Button) findViewById(R.id.btnFollowingEvent);
        buttonLogout = (Button) findViewById(R.id.btnLogout);


        username = SessionManager.getCookiesPref(getApplicationContext(), SessionManager.SP_USERNAME);
        Log.v("username",username);
        tvUsername.setText("Hallo "+username);

        buttonMyEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyEventActivity.class);
                startActivity(intent);
            }
        });

        buttonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you realy wan to exit?");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                SessionManager.clearAll(getApplicationContext());
                                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No",dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }
}
