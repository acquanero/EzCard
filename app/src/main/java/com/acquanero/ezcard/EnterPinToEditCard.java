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
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToEditCard extends AppCompatActivity {

    AppGeneralUseData generalData = new AppGeneralUseData();

    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;

    private EzCardApiService myAPIService;

    private Button buttonEditConfirm, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_edit_card);

        //Instancio el sharedPreference
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        Bundle datos = getIntent().getExtras();

        final String nameCard = datos.getString("cardName");
        final int cardId = datos.getInt("cardId");
        final int cardIconNumber = datos.getInt("cardIcon");

        final String theToken = dataDepot.getString("token", "null");
        final int userID = dataDepot.getInt("user_id", -1);

        System.out.println("nombre: " + nameCard);
        System.out.println("card id: " + cardId);
        System.out.println("icon: " + cardIconNumber);

        editPIN = (TextView) findViewById(R.id.inputPin);

        buttonCanel = (Button) findViewById(R.id.cancelEditButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonEditConfirm = (Button) findViewById(R.id.confirmEditButton);
        buttonEditConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();
                } else {
                    sendConfirmEditCard(theToken, pinEntered, userID, cardId, nameCard, cardIconNumber);
                }
            }
        });
    }

    public void sendConfirmEditCard(String token, int pin, int userid, int cardid, String cardName, int cardIcon){

        final String tokken = token;
        final int pinn = pin;
        final int useridd = userid;
        final int cardId = cardid;
        final String namecard = cardName;
        final int cardiconn = cardIcon;
        final Context context = this;

        myAPIService.putCard(generalData.appId, tokken, pinn, useridd, cardId, namecard, cardiconn).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    String userJson = dataDepot.getString("usuario", "null");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(userJson, UserData.class);

                    //recorro la lista de cards del usuario y cuando encuentro la que coincide con el id, le cambio los atributos
                    for (Card c : userData.getCards()){
                        if(c.getCardId() == cardId){
                            c.setName(namecard);
                            c.setIcon(cardiconn);
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
                    Toast t3 = Toast.makeText(context, getString(R.string.error_editing_card) , Toast.LENGTH_LONG);
                    t3.setGravity(Gravity.CENTER,0,0);
                    t3.show();
                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast t3 = Toast.makeText(context, getString(R.string.error_editing_card) , Toast.LENGTH_LONG);
                t3.setGravity(Gravity.CENTER,0,0);
                t3.show();

            }
        });

    }
}