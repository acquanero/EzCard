package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
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
import com.acquanero.ezcard.model.UserIdToken;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;
    private TextView editName, editSurname, editMailAdress, editPassw, editPhone, editPin;
    private Button buttonSignIn;
    SharedPreferences dataDepot;
    SharedPreferences.Editor dataDepotEditable;
    AppGeneralUseData generalData = new AppGeneralUseData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //recupero del layout los botones y los campos de texto
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        editName = (TextView) findViewById(R.id.editName);
        editSurname = (TextView) findViewById(R.id.editSurname);
        editMailAdress = (TextView) findViewById(R.id.editMailAdress);
        editPassw = (TextView) findViewById(R.id.editPassw);
        editPhone = (TextView) findViewById(R.id.editPhone);
        editPin = (TextView) findViewById(R.id.editPin);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String surname = editSurname.getText().toString();
                String mail = editMailAdress.getText().toString();
                String passw = editPassw.getText().toString();
                String phone = editPhone.getText().toString();
                int pin = Integer.parseInt(editPin.getText().toString());

                signIn(name,surname,mail,passw,phone,pin);

            }
        });
    }

    private void signIn(String name, String surname, String mail, String password, String phone, int pin){

        myAPIService.postToRegister(generalData.appId,name,surname,mail,password,phone,pin).enqueue(new Callback<UserIdToken>() {
            @Override
            public void onResponse(Call<UserIdToken> call, Response<UserIdToken> response) {

                if(response.isSuccessful()) {

                    //guardo el id y el token en una variable
                    int idUsuario = response.body().getUserId();
                    String token = response.body().getToken();

                    //Vuelvo editable mi SharedPreference
                    dataDepotEditable = dataDepot.edit();

                    //almaceno el id y el token en el SharedPreference
                    dataDepotEditable.putInt("user_id", idUsuario);
                    dataDepotEditable.putString("token", token);
                    dataDepotEditable.apply();

                    Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());

                }

            }

            @Override
            public void onFailure(Call<UserIdToken> call, Throwable t) {

            }
        });

    }
}