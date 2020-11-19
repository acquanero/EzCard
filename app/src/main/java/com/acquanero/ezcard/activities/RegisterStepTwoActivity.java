package com.acquanero.ezcard.activities;

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
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.models.Card;
import com.acquanero.ezcard.models.Provider;
import com.acquanero.ezcard.models.UserData;
import com.acquanero.ezcard.models.UserIdToken;
import com.acquanero.ezcard.myutils.MyHashGenerator;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepTwoActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;
    private EditText editPassw, editPin;
    private Button buttonSignIn;
    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;
    AppGeneralUseData generalData = new AppGeneralUseData();

    private int passwordMin = 6;
    private int passwordMax = 20;

    private int pinMin = 4;
    private int pinMax = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_two);



        //recupero del layout los botones y los campos de texto
        buttonSignIn = findViewById(R.id.buttonSignIn);
        editPassw = findViewById(R.id.editPassw);
        editPin = findViewById(R.id.editPin);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle datos = getIntent().getExtras();

                String name = datos.getString("name");
                String last_name = datos.getString("lastName");
                String email = datos.getString("mail");
                String password = editPassw.getText().toString();
                String cellphone = datos.getString("phone");
                String pin = editPin.getText().toString();

                signIn(name,last_name,email,password,cellphone,pin);

            }
        });
    }

    private void signIn(String name, String surname, String mail, String password, String phone, String pin){

        if (validateFields(password,pin)){

            final Context context = this;
            String hashPin = null;
            String hashPassword = null;

            try {
                hashPin = MyHashGenerator.hashString(pin);
                hashPassword = MyHashGenerator.hashString(password);

            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();
            }

            final String theName = name;
            final String theSurname = surname;
            final String theEmail = mail;
            final String thePhone = phone;

            myAPIService.postToRegister(generalData.appId,name,surname,hashPassword,mail,phone,hashPin).enqueue(new Callback<UserIdToken>() {
                @Override
                public void onResponse(Call<UserIdToken> call, Response<UserIdToken> response) {

                    if(response.isSuccessful()) {

                        //guardo el id y el token en una variable
                        int idUsuario = response.body().getUserId();
                        String token = response.body().getToken();

                        //Creo un objeto UserData para guardar en el sharedpreference toda la info del usuario
                        UserData userData = new UserData();
                        userData.setName(theName);
                        userData.setLastName(theSurname);
                        userData.setEmail(theEmail);
                        userData.setCellphone(thePhone);
                        userData.setUserId(idUsuario);
                        userData.setEnabled(true);
                        userData.setCards(new ArrayList<Card>());
                        userData.setProviders(new ArrayList<Provider>());


                        //Vuelvo editable mi SharedPreference
                        dataDepotEditable = dataDepot.edit();

                        //almaceno el id y el token en el SharedPreference
                        dataDepotEditable.putInt("user_id", idUsuario);
                        dataDepotEditable.putString("token", token);

                        //Convierto el obejto userData a String para almacenarlo
                        Gson gson = new Gson();
                        String json = gson.toJson(userData);
                        dataDepotEditable.putString("usuario", json);

                        dataDepotEditable.apply();

                        Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());

                        Toast t = Toast.makeText(context, getString(R.string.msg_register_success) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();

                        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                        startActivity(i);

                    } else {

                        if (response.body() != null) {

                            Log.i("RTA FAIL", "Fail to post the info to register" + response.body().toString());

                        }

                        Toast t = Toast.makeText(context, getString(R.string.msg_register_fail) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();

                        Intent i = new Intent(context, LogInActivity.class);
                        startActivity(i);

                    }

                }




                @Override
                public void onFailure(Call<UserIdToken> call, Throwable t) {

                    Toast toast = Toast.makeText(context, getString(R.string.msg_register_fail) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);

                }
            });
        }

    }

    private boolean validateFields(String password,String pin) {

        boolean result = true;

        if (!MyValidators.isBetween(password,passwordMin,passwordMax)){
            Toast t1 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_passw) , Toast.LENGTH_LONG);
            t1.setGravity(Gravity.CENTER,0,0);
            t1.show();
            result=false;

        }
        if (!MyValidators.isBetween(pin,pinMin,pinMax) || !MyValidators.isOnlyNumber(pin)){
            Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
            t2.setGravity(Gravity.CENTER,0,0);
            t2.show();
            result=false;
        }
        return result;
    }
}