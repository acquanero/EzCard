package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.myutils.MyHashGenerator;
import com.acquanero.ezcard.myutils.MyValidators;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePinActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;

    SharedPreferences dataDepot;
    AppGeneralUseData generalData = new AppGeneralUseData();

    private int pinMin = 4;
    private int pinMax = 4;

    private EditText oldPinEdit, newPinEditOne, newPinEditTwo;
    private Button buttonAcept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        //Creo una instancia de SahredPreference para recuperar informacion
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Recupero el token y el userId
        final String theToken = dataDepot.getString("token", "null");
        final int userID = dataDepot.getInt("user_id", -1);

        //Instancio los componentes graficos
        oldPinEdit = findViewById(R.id.inputOldPin);
        newPinEditOne = findViewById(R.id.inputNewPinOne);
        newPinEditTwo = findViewById(R.id.inputNewPinTwo);

        buttonAcept = findViewById(R.id.buttonSendNewPin);

        buttonAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pinOld = oldPinEdit.getText().toString();
                String pinNewFirst = newPinEditOne.getText().toString();
                String pinNewSecond = newPinEditTwo.getText().toString();

                if(!validatePin(pinOld) || !validatePin(pinNewFirst) || !validatePin(pinNewSecond)){
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();

                } else if(!pinNewFirst.equalsIgnoreCase(pinNewSecond)){

                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.reconfirmation_doesnt_match) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();

                } else {

                    int myOldPin = Integer.parseInt(pinOld);
                    int myNewPin = Integer.parseInt(pinNewFirst);

                    sendPutNewPin(theToken, myOldPin, userID, myNewPin );
                }

            }
        });

    }

    private boolean validatePin(String pin) {

        boolean result = true;

        if (!MyValidators.isBetween(pin,pinMin,pinMax) || !MyValidators.isOnlyNumber(pin)){
            result=false;
        }

        return result;
    }

    private void sendPutNewPin(String token, int pin, int userId ,int newPin){

        final Context context = this;
        final String tokenn = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int iduser = userId;
        final int theNewPin = newPin;

        myAPIService.updatePin(generalData.appId, tokenn, hashPin, iduser, theNewPin).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    Toast toast = Toast.makeText(context, getString(R.string.success_change_pin) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                    //Vuelvo a la vista de drawer
                    Intent goToMain = new Intent(context, MainDrawerActivity.class);
                    startActivity(goToMain);

                } else {

                    Toast toast = Toast.makeText(context, getString(R.string.error_change_pin) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                }


            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast toast = Toast.makeText(context, getString(R.string.error_change_pin) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();


            }
        });

    }
}