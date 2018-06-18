package com.example.ardo.eventz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardo.eventz.R;
import com.example.ardo.eventz.networking.BaseApiService;
import com.example.ardo.eventz.networking.UtilsApi;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonRegister;
    TextView textViewLogin;
    ProgressDialog progressDialog;
    Context mContext;
    BaseApiService mBaseApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mBaseApiService = UtilsApi.getApiService(); // meng-init yang ada di package apihelper
        editTextUsername = findViewById(R.id.etUsername);
        editTextFirstName = findViewById(R.id.etFirst_name);
        editTextLastName = findViewById(R.id.etLast_name);
        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);
        buttonRegister = findViewById(R.id.btnRegister);
        textViewLogin = findViewById(R.id.tvLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(mContext, null, "Please Wait...",true, false);
                requestRegister();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });
    }

    private void requestRegister() {
        mBaseApiService.registerRequest(editTextUsername.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.i("debug", "onResponse Success");
                            Toast.makeText(mContext, "Register Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }, 3000);

                        } else {
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(mContext, "This password is too short. It must contain at least 8 characters. And field not be a blank",
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case 500:
                                    Toast.makeText(mContext, "Register failed, someone with that username or email has already registered. Was it you?",
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(mContext, "Register Failed", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            Log.i("debug", "onResponse Failed");
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure Error > " + t.getMessage());
                        Toast.makeText(mContext, "No Internet Connections", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
