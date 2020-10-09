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
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserIdToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;

    private TextView mailUser;
    private TextView password;

    private UserIdToken useridtoken;

    SharedPreferences dataDepot;

    SharedPreferences.Editor dataDepotEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //En esta seccion deberia chequear si ya estoy logueado (tengo token)
        //Si (hay token) =>
        //Ver si tengo tarjetas agregadas
        //Sin tarjetas => ir a AgregadoDeTarjetas Activity
        //Con Tarjetas => ir a VistaDeServicios Activity

        if(!dataDepot.getString("token", "null").equalsIgnoreCase("null")){

            Intent goToCardsActivity = new Intent(this, CardsActivity.class);

            startActivity(goToCardsActivity);
        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recupero del layout los botones y los campos de texto
        Button loginButton = (Button) findViewById(R.id.button_login);
        mailUser = (TextView) findViewById(R.id.campo_usuario);
        password = (TextView) findViewById(R.id.campo_password);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();

        //asocio el evento correspondiente al boton de login
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                logIn(mailUser.getText().toString(), password.getText().toString());


            }
        });
    }

    //metodo a ejecutar al presionar el boton login
    public void logIn(String mail, String passw) {

        myAPIService.postDataGetToken(mail, passw).enqueue(new Callback<UserIdToken>() {
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

                    //Chequeo que se hayan almacenado
                    System.out.println("----------------------------------");
                    System.out.println("User id: " + dataDepot.getInt("user_id", -1) + " Token: " + dataDepot.getString("token", "null"));
                    System.out.println("----------------------------------");

                    Log.i("RTA SUCCESS", "post submitted to API." + response.body().toString());

                } else {

                    if(response.code() == 401){
                        Context context = getApplicationContext();
                        Toast t = Toast.makeText(context, getString(R.string.user_mail_erro_msg) , Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                        t.show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserIdToken> call, Throwable t) {

                Log.e("RTA FAIL", "Unable to submit post to API.");
            }
        });
    }


}