package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyHashGenerator;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
    private TextView txtTitle, labelCardProvider;

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
        labelCardProvider= findViewById(R.id.labelCardProvider);

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

            //3° rama del If. Flag me envia a Ingresar el PIN para eliminar una tarjeta

        } else if (flag.equalsIgnoreCase("enterPinToDeleteCard")) {

            final int idCard = datos.getInt("cardid");
            final int userID = dataDepot.getInt("user_id", -1);
            final String cardName = datos.getString("cardName");

            txtTitle.setText(getString(R.string.enter_pin_to_delete_card));
            labelCardProvider.setText(cardName);

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
                        confirmDeleteCard(theToken, pinEntered, userID, idCard);
                    }
                }
            });

            //4° rama del If. Flag me envia a Ingresar el PIN para eliminar un servicio

        } else if(flag.equalsIgnoreCase("enterPinToDeleteProvider")){

            providerId = datos.getInt("providerId");

            String nameOfProvider = datos.getString("providerName");

            txtTitle.setText(getString(R.string.enter_pin_to_delete_provider));
            labelCardProvider.setText(nameOfProvider);

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
                        confirmDeleteProvider(theToken, pinEntered, providerId);
                    }
                }
            });

            //5° rama del If. Flag me envia a Ingresar el PIN para modificar los datos de una tarjeta

        } else if (flag.equalsIgnoreCase("enterPinToEditCard")){

            txtTitle.setText(getString(R.string.enter_pin_to_edit_card));

            final String nameCard = datos.getString("cardName");
            final int cardId = datos.getInt("cardId");
            final int cardIconNumber = datos.getInt("cardIcon");

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
                        sendConfirmEditCard(theToken, pinEntered, userID, cardId, nameCard, cardIconNumber);
                    }
                }
            });

            //6° rama del If. Flag me envia a Ingresar el PIN para desvincular un servicio de la tarjeta

        } else if(flag.equalsIgnoreCase("enterPinToUnbindProvider")){

            txtTitle.setText(getString(R.string.Enter_pin_to_disassociate_service));

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
                        confirmDisassociateService(theToken, pinEntered, providerId);
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

    private void confirmDeleteCard(String token, int pin, int userid, int cardid) {

        final String tokken = token;
        final int pinn = pin;
        final int useridd = userid;
        final int cardidd = cardid;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.warning_delete_card_title);
        builder.setMessage(R.string.warning_delete_card_msg);
        builder.setPositiveButton(R.string.acept_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteCard(tokken, pinn, useridd, cardidd);

            }
        });
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.show();
    }

    private void deleteCard(String token, int pin, int userid, int cardid){

        final Context context = this;
        final String theToken = token;

        String pinString = Integer.toString(pin);
        String hashPin = null;

        try {

            hashPin = MyHashGenerator.hashString(pinString);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }


        final int theuserID = userid;
        final int cardId = cardid;

        myAPIService.deleteCard(generalData.appId, theToken, hashPin, theuserID, cardId).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    String userJson = dataDepot.getString("usuario", "null");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(userJson, UserData.class);

                    ArrayList<Card> listaCards = new ArrayList<Card>();

                    //recorro la lista de cards del usuario y las agrego a un nuevo array, excepto la que tiene el id que voy a eliminar
                    for (Card c : userData.getCards()){
                        if(c.getCardId() != cardId){
                            listaCards.add(c);
                        }
                    }

                    //le setteo la nueva lista de tarjetas que no posee la tarjeta eliminada
                    userData.setCards(listaCards);

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

                    Toast t3 = Toast.makeText(context, getString(R.string.delete_card_error) , Toast.LENGTH_LONG);
                    t3.setGravity(Gravity.CENTER,0,0);
                    t3.show();

                }


            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast t3 = Toast.makeText(context, getString(R.string.delete_card_error) , Toast.LENGTH_LONG);
                t3.setGravity(Gravity.CENTER,0,0);
                t3.show();

            }
        });

    }

    private void confirmDeleteProvider(String token, int pin, int providerId){

        final String tokken = token;
        final int pinn = pin;
        final int idProvider = providerId;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.warning_delete_provider_title);
        builder.setMessage(R.string.warning_delete_provider_msg);
        builder.setPositiveButton(R.string.acept_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteProvider(tokken, pinn, idProvider);

            }
        });
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.show();

    }

    private void deleteProvider(String token, int pin, int providerId){

        final Context context = this;
        final String tokenn = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int idprovider = providerId;

        myAPIService.deleteProvider(generalData.appId, tokenn, hashPin, idprovider).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    String userJson = dataDepot.getString("usuario", "null");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(userJson, UserData.class);

                    ArrayList<Provider> listaProviders = new ArrayList<Provider>();

                    //recorro la lista de cards del usuario y las agrego a un nuevo array, excepto la que tiene el id que voy a eliminar
                    for (Provider p : userData.getProviders()){
                        if(p.getProviderId() != idprovider){
                            listaProviders.add(p);
                        }
                    }

                    //le setteo la nueva lista de tarjetas que no posee la tarjeta eliminada
                    userData.setProviders(listaProviders);

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

                    Toast t = Toast.makeText(context, getString(R.string.delete_provider_error) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast tt = Toast.makeText(context, getString(R.string.delete_provider_error) , Toast.LENGTH_LONG);
                tt.setGravity(Gravity.CENTER,0,0);
                tt.show();

            }
        });

    }

    private void sendConfirmEditCard(String token, int pin, int userid, int cardid, String cardName, int cardIcon){

        final String tokken = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int useridd = userid;
        final int cardId = cardid;
        final String namecard = cardName;
        final int cardiconn = cardIcon;
        final Context context = this;

        myAPIService.putCard(generalData.appId, tokken, hashPin, useridd, cardId, namecard, cardiconn).enqueue(new Callback<SimpleResponse>() {
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

    private void confirmDisassociateService(String token, int pin, int provider){

        final Context context = this;
        final String theTokken = token;

        String thePin = String.valueOf(pin);
        String hashPin = null;

        try {
            hashPin = MyHashGenerator.hashString(thePin);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        final int theProviderId = provider;

        myAPIService.unbindProvider(generalData.appId, theTokken, hashPin, theProviderId).enqueue(new Callback<SimpleResponse>() {
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