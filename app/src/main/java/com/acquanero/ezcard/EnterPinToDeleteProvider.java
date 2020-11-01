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

public class EnterPinToDeleteProvider extends AppCompatActivity {

    private EzCardApiService myAPIService;
    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;
    AppGeneralUseData generalData = new AppGeneralUseData();

    private Button buttonDelete, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    private int providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_delete_provider);

        Bundle datos = getIntent().getExtras();
        final String theToken = dataDepot.getString("token", "null");
        providerId = datos.getInt("providerId");

        editPIN = findViewById(R.id.inputPinDeleteProvider);

        buttonCanel = findViewById(R.id.cancelDeleteProviderButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonDelete = findViewById(R.id.confirmDeleteProviderButton);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

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

    public void deleteProvider(String token, int pin, int providerId){

        final Context context = this;
        final String tokenn = token;
        final int elPin = pin;
        final int idprovider = providerId;

        myAPIService.deleteProvider(generalData.appId, tokenn, elPin, idprovider).enqueue(new Callback<SimpleResponse>() {
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
}