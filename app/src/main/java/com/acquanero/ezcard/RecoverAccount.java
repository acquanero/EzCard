package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.myutils.MyValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoverAccount extends AppCompatActivity {

    private EzCardApiService myAPIService;
    private TextView mailUser;
    private Button buttonRecover;
    AppGeneralUseData generalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_account);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        generalData = new AppGeneralUseData();

        mailUser = (TextView) findViewById(R.id.editMailRecover);
        buttonRecover = (Button) findViewById(R.id.buttonRecover);

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recoverAccount(mailUser.getText().toString());
            }
        });
    }

    private void recoverAccount(String mail){

        final Context context = this;

        if(!MyValidators.isValidEmail(mail)){

            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_email) , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();

        } else {

            myAPIService.postToRecover(generalData.appId, mail).enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                    if(response.isSuccessful()) {

                        Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());

                        Toast t = Toast.makeText(context, getString(R.string.msg_recover_success) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();


                    } else {

                        Toast t = Toast.makeText(context, getString(R.string.msg_recover_fail) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();


                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {

                    Log.e("RTA FAIL", "----Recovery failed------");

                }
            });


        }

    }
}