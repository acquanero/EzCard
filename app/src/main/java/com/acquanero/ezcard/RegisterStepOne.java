package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserIdToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepOne extends AppCompatActivity {

    private TextView editName, editSurname, editMailAdress, editPhone;
    private Button buttonNext;
    SharedPreferences dataDepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);

        editName = (TextView) findViewById(R.id.editName);
        editSurname = (TextView) findViewById(R.id.editSurname);
        editMailAdress = (TextView) findViewById(R.id.editMailAdress);
        editPhone = (TextView) findViewById(R.id.editPhone);

        buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToStepTwo();

            }
        });

    }

    private  void goToStepTwo(){

        Intent i = new Intent(getApplicationContext(), RegisterStepTwo.class);

        i.putExtra("name", editName.getText().toString());
        i.putExtra("lastName", editSurname.getText().toString());
        i.putExtra("mail", editMailAdress.getText().toString());
        i.putExtra("phone", editPhone.getText().toString());

        startActivity(i);



    }

}