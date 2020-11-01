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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToAssociateService extends AppCompatActivity {

    private int providerId;
    private int numIdCard;

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
        setContentView(R.layout.activity_enter_pin_to_associate_service);

        //Instancio el sharedPreference
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        Bundle datos = getIntent().getExtras();
        numIdCard = datos.getInt("cardId");
        providerId = datos.getInt("providerId");

        final String theToken = dataDepot.getString("token", "null");

        editPIN = findViewById(R.id.inputPinToAssociateService);

        buttonCanel = findViewById(R.id.cancelAssociateServiceButton);

        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonConfirm = findViewById(R.id.confirmAssociateServiceButton);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                } else {
                    postToAssociateService(theToken, pinEntered, numIdCard, providerId);
                }

            }
        });
    }

    private void postToAssociateService(String token, int pin, int cardId, int providerId){

        final String tokken = token;
        final int pinn = pin;
        final int idcard = cardId;
        final int idProvider = providerId;
        final Context context = this;

        myAPIService.bindProvider(generalData.appId, tokken, pinn, idcard, idProvider).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    String userJson = dataDepot.getString("usuario", "null");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(userJson, UserData.class);

                    //recorro la lista de cards del usuario y cuando encuentro la que coincide con el id, le cambio los atributos
                    for (Provider p : userData.getProviders()){
                        if(p.getProviderId() == idProvider){

                            p.setCardId(idcard);
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

                    Toast toast = Toast.makeText(context, getString(R.string.error_while_binding_the_service) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                }


            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast toast = Toast.makeText(context, getString(R.string.error_while_binding_the_service) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

            }
        });


    }
}