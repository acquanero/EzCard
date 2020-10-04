package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserIdToken;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;

    private TextView mailUser;
    private TextView password;

    private  Gson gson = new Gson();

    private UserIdToken useridtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.button_login);

        mailUser = (TextView) findViewById(R.id.campo_usuario);
        password = (TextView) findViewById(R.id.campo_password);

        myAPIService = ApiUtils.getAPIService();

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                logIn(mailUser.getText().toString(), password.getText().toString());


            }
        });
    }

    public void logIn(String mail, String passw) {
        myAPIService.getUserInfo(mail, passw).enqueue(new Callback<UserIdToken>() {
            @Override
            public void onResponse(Call<UserIdToken> call, Response<UserIdToken> response) {

                if(response.isSuccessful()) {
                    int id = response.body().getUserId();
                    String token = response.body().getToken();

                    System.out.println("----------------------------------");
                    System.out.println("User id: " + id + " Token: " + token);
                    System.out.println("----------------------------------");

                    Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<UserIdToken> call, Throwable t) {
                Log.e("RTA FAIL", "Unable to submit post to API.");
            }
        });
    }


}