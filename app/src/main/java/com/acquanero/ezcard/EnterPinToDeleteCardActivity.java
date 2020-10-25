package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinToDeleteCardActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;
    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;
    AppGeneralUseData generalData = new AppGeneralUseData();

    private TextView labelCardName;
    private Button buttonDelete, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_delete_card);

        Bundle datos = getIntent().getExtras();

        final int idCard = datos.getInt("cardid");
        final String cardName = datos.getString("cardName");
        final String theToken = dataDepot.getString("token", "null");
        final int userID = dataDepot.getInt("user_id", -1);

        editPIN = (TextView) findViewById(R.id.inputPin);

        labelCardName = (TextView) findViewById(R.id.labelCardName);
        labelCardName.setText(cardName);


        buttonCanel = (Button) findViewById(R.id.cancelDeleteButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonDelete = (Button) findViewById(R.id.confirmDeleteButton);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();

                    System.out.println(pinEntered);
                } else {
                    confirmDeleteCard(theToken, pinEntered, userID, idCard);
                }
            }
        });

    }

    public void confirmDeleteCard(String token, int pin, int userid, int cardid) {

        final Context context = this;
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

    public void deleteCard(String token, int pin, int userid, int cardid){

        final Context context = this;
        final String theToken = token;
        final int elPin = pin;
        final int theuserID = userid;
        final int cardId = cardid;

        myAPIService.deleteCard(generalData.appId, theToken, elPin, theuserID, cardId).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

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

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

            }
        });

    }
}