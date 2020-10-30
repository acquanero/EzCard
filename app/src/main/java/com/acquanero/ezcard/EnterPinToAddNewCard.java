package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToAddNewCard extends AppCompatActivity {

    AppGeneralUseData generalData = new AppGeneralUseData();

    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;

    private EzCardApiService myAPIService;

    private Button buttonConfirm, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_add_new_card);

        //Instancio el sharedPreference
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        Bundle datos = getIntent().getExtras();

        final String nameCard = datos.getString("nameCard");
        final int iconNumber = datos.getInt("iconNumber");
        final String tag = datos.getString("tag");

        final String theToken = dataDepot.getString("token", "null");
        final int userID = dataDepot.getInt("user_id", -1);

        editPIN = (TextView) findViewById(R.id.inputPinForNewCard);

        buttonCanel = findViewById(R.id.cancelAddCardButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonConfirm = findViewById(R.id.confirmAddCardButton);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();
                } else {
                    sendDataForNewCard(theToken, pinEntered, userID, nameCard, iconNumber, tag);
                }

            }
        });

        System.out.println("Nombre: " + nameCard);
        System.out.println("icon number: " + iconNumber);
        System.out.println("TAG: " + tag);

    }

    private void sendDataForNewCard(String token, int pin, int userid, String cardName, int cardIcon, String theTag){

        final String tokken = token;
        final int pinn = pin;
        final int useridd = userid;
        final String namecard = cardName;
        final int cardiconn = cardIcon;
        final String cardTag = theTag;
        final Context context = this;

        myAPIService.postNewCard(generalData.appId, tokken, pinn, useridd, cardTag, namecard, cardiconn).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    //Vuelvo a la vista de drawer
                    Intent goToMain = new Intent(context, MainActivity.class);
                    startActivity(goToMain);

                } else {

                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, getString(R.string.error_while_trying_to_add_card) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                    Log.e("RTA FAIL", "Unable to submit post to API.");
                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, getString(R.string.error_while_trying_to_add_card) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

                Log.e("RTA FAIL", "Unable to submit post to API.");


            }
        });

    }
}