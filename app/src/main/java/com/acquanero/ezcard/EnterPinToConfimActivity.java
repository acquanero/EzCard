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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyHashGenerator;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToConfimActivity extends AppCompatActivity {

    AppGeneralUseData generalData = new AppGeneralUseData();

    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;

    private EzCardApiService myAPIService;

    private Button buttonAcept, buttonCanel;
    private TextView editPIN;
    private TextView txtTitle;

    private int pinMin = 4;
    private int pinMax = 4;

    private int providerId;
    private int numIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_confim);

        //Instancio el sharedPreference
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        final String theToken = dataDepot.getString("token", "null");

        txtTitle = findViewById(R.id.textTitle);

        buttonCanel = findViewById(R.id.cancelButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        editPIN = findViewById(R.id.editBoxEnterPin);

        buttonAcept = findViewById(R.id.aceptButton);

        //Saco del bundle el Flag que me dirá que debe hacer la Activity al ingresar el PIN
        Bundle datos = getIntent().getExtras();
        String flag = datos.getString("flag");

        //1° rama del If. Flag me envia a Ingresar el PIN para agregar tarjeta

        if (flag.equalsIgnoreCase("enterPinToAddNewCard")) {

            txtTitle.setText(getString(R.string.enter_pin_to_add_new_card));

            final String nameCard = datos.getString("nameCard");
            final int iconNumber = datos.getInt("iconNumber");
            final String tag = datos.getString("tag");
            final int userID = dataDepot.getInt("user_id", -1);

            buttonAcept.setOnClickListener(new View.OnClickListener() {
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

            //2° rama del If. Flag me envia a Ingresar el PIN para asociar un servicio

        } else if (flag.equalsIgnoreCase("enterPinToBindProvider")){

            txtTitle.setText(getString(R.string.enter_pin_to_associate_service));

            numIdCard = datos.getInt("cardId");
            providerId = datos.getInt("providerId");

            buttonAcept.setOnClickListener(new View.OnClickListener() {
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

    }

    private void sendDataForNewCard(String token, int pin, int userid, String cardName, int cardIcon, String theTag){

        final String tokken = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int useridd = userid;
        final String namecard = cardName;
        final int cardiconn = cardIcon;
        final String cardTag = theTag;
        final Context context = this;

        myAPIService.postNewCard(generalData.appId, tokken, hashPin, useridd, cardTag, namecard, cardiconn).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    //Vuelvo a la vista de drawer
                    Intent goToMain = new Intent(context, MainActivity.class);
                    startActivity(goToMain);

                } else {

                    Toast toast = Toast.makeText(context, getString(R.string.error_while_trying_to_add_card) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                    Log.e("RTA FAIL", "Unable to submit post to API.");
                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast toast = Toast.makeText(context, getString(R.string.error_while_trying_to_add_card) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

                Log.e("RTA FAIL", "Unable to submit post to API.");


            }
        });

    }

    private void postToAssociateService(String token, int pin, int cardId, int providerId){

        final String tokken = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int idcard = cardId;
        final int idProvider = providerId;
        final Context context = this;

        myAPIService.bindProvider(generalData.appId, tokken, hashPin, idcard, idProvider).enqueue(new Callback<SimpleResponse>() {
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