package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToDisassociateService extends AppCompatActivity {

    private EzCardApiService myAPIService;
    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;
    AppGeneralUseData generalData = new AppGeneralUseData();

    private TextView labelCardName;
    private Button buttonDissasociate, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    private int providerId;

    private String theToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        theToken = dataDepot.getString("token", "null");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_disassociate_service);

        Bundle datos = getIntent().getExtras();
        providerId = datos.getInt("providerId");

        editPIN = findViewById(R.id.inputPinDisassociate);

        buttonCanel = findViewById(R.id.cancelDisassociateButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonDissasociate = findViewById(R.id.confirmDisassociateButton);
        buttonDissasociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                } else {
                    confirmDisassociateService(theToken, pinEntered, providerId);
                }

            }
        });


    }

    private void confirmDisassociateService(String token, int pin, int provider){

        final Context context = this;
        final String theTokken = token;
        final int elPin = pin;
        final int theProviderId = provider;

        myAPIService.unbindProvider(generalData.appId, theTokken, elPin, theProviderId).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    String userJson = dataDepot.getString("usuario", "null");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(userJson, UserData.class);

                    for (Provider p : userData.getProviders()){
                        if(p.getProviderId() == theProviderId){
                            p.setCardId(null);
                        }
                    }

                    //Convierto nuevamente el usuario en String para almacenarlo
                    String json = gson.toJson(userData);

                    //Vuelvo editable mi SharedPreference
                    dataDepotEditable = dataDepot.edit();
                    dataDepotEditable.putString("usuario", json);
                    dataDepotEditable.apply();

                    //Vuelvo a la vista de drawer
                    Intent goToDrawer = new Intent(context, MainDrawer.class);
                    startActivity(goToDrawer);



                } else {

                    Toast t = Toast.makeText(context, getString(R.string.error_to_disassociate_service) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast to = Toast.makeText(context, getString(R.string.error_to_disassociate_service) , Toast.LENGTH_LONG);
                to.setGravity(Gravity.CENTER,0,0);
                to.show();

            }
        });


    }
}