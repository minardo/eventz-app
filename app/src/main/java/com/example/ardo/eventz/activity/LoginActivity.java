package com.example.ardo.eventz.activity;

import android.app.Activity;
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
import com.example.ardo.eventz.networking.RetrofitClient;
import com.example.ardo.eventz.utils.AddCookiesInterceptor;
import com.example.ardo.eventz.utils.SessionManager;
import com.example.ardo.eventz.networking.UtilsApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ardo.eventz.utils.AddCookiesInterceptor.PREF_COOKIES;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewRegister;
    ProgressDialog progressDialog;
    Context mContext;
    Activity context = this;
    BaseApiService mBaseApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RetrofitClient retrofitClient = new RetrofitClient(this);

        mContext = this;
        mBaseApiService = UtilsApi.getApiService(); // init yang ada di package networking
        editTextUsername = findViewById(R.id.etUsername);
        editTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.etLogin);
        textViewRegister = findViewById(R.id.etRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(mContext, null, "Please Wait...", true, false);
                requestLogin();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

        // Checking user login, if the user is login then skip the login activity
        String username = SessionManager.getCookiesPref(getApplicationContext(), SessionManager.SP_USERNAME);
        if (username !=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void requestLogin() {
        mBaseApiService.loginRequest(editTextUsername.getText().toString(), editTextPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // If login success, data will be parsing to next activity (dashboard activity)
                            RetrofitClient retrofitClient = new RetrofitClient(context);
                            Log.i("debug", "onResponse Success");
                            Toast.makeText(mContext, "Login Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            String username = editTextUsername.getText().toString();
                            SessionManager.saveSPString(getApplicationContext(), SessionManager.SP_USERNAME,username);
                            SessionManager.saveSPString(getApplicationContext(), PREF_COOKIES, PREF_COOKIES);
                            startActivity(new Intent(mContext, MainActivity.class)
                                    .putExtra(username, editTextUsername.getText().toString())
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();

                        } else {
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(mContext, "Login invalid, check your username or password and can't be empty",
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(mContext, "Login Failed",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            Log.i("debug", "onResponse Failed");
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure Error > " + t.toString());
                        progressDialog.dismiss();
                    }
                });
    }
}
