package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;

    private TextView mailUser;
    private TextView password;

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

                sendPost(mailUser.getText().toString(), password.getText().toString());


            }
        });
    }

    public void sendPost(String mail, String passw) {
        myAPIService.getUserInfo(mail, passw).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.e("RTA FAIL", "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(String response) {
        System.out.println("------------");
        System.out.println(response);
        System.out.println("------------");
    }

}