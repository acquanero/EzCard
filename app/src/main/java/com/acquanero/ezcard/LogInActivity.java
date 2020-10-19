
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
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInActivity extends AppCompatActivity {

    private int passwordMin = 6;
    private int passwordMax = 12;

    private EzCardApiService myAPIService;
    private TextView mailUser, password, register, recover;
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
        setContentView(R.layout.activity_log_in);

        //recupero del layout los botones y los campos de texto
        Button loginButton = (Button) findViewById(R.id.button_login);
        mailUser = (TextView) findViewById(R.id.campo_usuario);
        password = (TextView) findViewById(R.id.campo_password);
        register = (TextView) findViewById(R.id.label_register);
        recover = (TextView) findViewById(R.id.label_forgot_password);

        //asocio el evento correspondiente al boton de login
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                logIn(mailUser.getText().toString(), password.getText().toString());


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), RegisterStepOne.class);
                startActivity(i);

            }
        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), RecoverAccount.class);
                startActivity(i);

            }
        });
    }

    //metodo a ejecutar al presionar el boton login
    public void logIn(String mail, String passw) {

        if(!MyValidators.isBetween(passw,passwordMin,passwordMax) || MyValidators.isValidEmail(mail) == false){

            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_email_or_passw) , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();

        } else {

            myAPIService.postDataGetToken(generalData.appId, mail, passw).enqueue(new Callback<UserIdToken>() {
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

                        getUserWholeData(token, idUsuario);

                    } else {

                        if(response.code() == 401){
                            Context context = getApplicationContext();
                            Toast t = Toast.makeText(context, getString(R.string.user_mail_erro_msg) , Toast.LENGTH_LONG);
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

    public void getUserWholeData(String token, int userid) {

        final Context context = this;

        myAPIService.getUserData(generalData.appId, token, userid ).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                //Vuelvo editable mi SharedPreference
                dataDepotEditable = dataDepot.edit();

                //almaceno los datos del usuario en el sharedPreference

                UserData user = new UserData();
                user.setName(response.body().getName());
                user.setLastName(response.body().getLastName());
                user.setPassword(response.body().getPassword());
                user.setEmail(response.body().getEmail());
                user.setCellphone(response.body().getCellphone());
                user.setUserId(response.body().getUserId());
                user.setEnabled(response.body().getEnabled());
                user.setCards(response.body().getCards());
                user.setProviders(response.body().getProviders());

                Gson gson = new Gson();
                String json = gson.toJson(user);

                dataDepotEditable.putString("usuario", json);

                dataDepotEditable.apply();

                //me traigo la lista de tarjetas del usuario, y chequeo si tiene tarjetas asociadas
                //para redireccionar a la activity correspondiente
                List<Card> myCardList = response.body().getCards();

                //Ver si tengo tarjetas agregadas
                //Sin tarjetas => ir a AgregadoDeTarjetas Activity
                //Con Tarjetas => ir a VistaDeServicios Activity

                if(myCardList.size() == 0) {

                    Intent goToCardsActivity = new Intent(context, CardsActivity.class);

                    startActivity(goToCardsActivity);

                } else {

                    Intent goToServiceActivity = new Intent(context, ServiceActivity.class);

                    startActivity(goToServiceActivity);
                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

                Log.e("RTA FAIL", "----Fallo en traer la informacion del usuario------");

            }
        });

    }

}