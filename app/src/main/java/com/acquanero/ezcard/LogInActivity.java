
package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;
import com.acquanero.ezcard.myutils.MyValidators;
import com.google.gson.Gson;


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

    private ProgressBar loadingBar;
    private ConstraintLayout mainLayout;

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

        mainLayout = findViewById(R.id.loginLayout);


        //asocio el evento correspondiente al boton de login
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // mostrar circulo de progreso mientras carga
                ConstraintSet set = new ConstraintSet();
                loadingBar = new ProgressBar(LogInActivity.this, null, android.R.attr.progressBarStyleLarge);

                //tooodo este choclo es solo para definir la posicion del loadingBar en el centro del ConstraintLayout
                loadingBar.setId(View.generateViewId());
                mainLayout.addView(loadingBar,0);

                set.clone(mainLayout);
                set.connect(loadingBar.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(loadingBar.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                set.connect(loadingBar.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                set.connect(loadingBar.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                set.applyTo(mainLayout);

                loadingBar.setVisibility(View.VISIBLE);

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

                        } else {
                            Context context = getApplicationContext();
                            Toast t = Toast.makeText(context, getString(R.string.error_log_in) , Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                            t.show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<UserIdToken> call, Throwable t) {

                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, getString(R.string.error_log_in) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

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

                if(response.isSuccessful()) {

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

                    //Al terminar oculto el loading Bar
                    loadingBar.setVisibility(View.GONE);

                    Intent goToCardsActivity = new Intent(context, MainDrawer.class);

                    startActivity(goToCardsActivity);

                } else {

                    //Al terminar oculto el loading Bar
                    loadingBar.setVisibility(View.GONE);

                    Context context = getApplicationContext();
                    Toast t = Toast.makeText(context, getString(R.string.error_while_getting_user_data) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();

                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

                //Al terminar oculto el loading Bar
                loadingBar.setVisibility(View.GONE);

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, getString(R.string.error_while_getting_user_data) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

                Log.e("RTA FAIL", "----Fallo en traer la informacion del usuario------");

            }
        });

    }

}