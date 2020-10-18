package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;
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

        //cambio el token almacenado para debugguear (token abc123 entra, con otro el server devuelve 401 error)
        //dataDepotEditable = dataDepot.edit();
        //dataDepotEditable.putString("token", "fff");
        //dataDepotEditable.apply();

        String token = dataDepot.getString("token", "null");
        int userID = dataDepot.getInt("user_id", -1);


        //Si ni tiene guardado el user id o el token, lo envia directo al log in activity
        if(token.equalsIgnoreCase("null") || userID == -1){

            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);

        } else {
            //intento loguearme con token
            logWithToken(token, userID);

        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //metodo para log in con el token
    public void logWithToken(String token, int userid){

        final Context context = this;
        final String theToken = token;
        final int theuserID = userid;

        myAPIService.logInWithToken(generalData.appId, token, userid).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if(response.code() == 200) {

                    getUserWholeData(theToken, theuserID);

                    Log.i("RTA SUCCESS", "post submitted to API." + response.body().getMessage());


                } else {

                    if(response.code() == 401){
                        Toast t = Toast.makeText(context, getString(R.string.login_again_msg) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();

                        System.out.println("-----------Error 401------!!!!!");

                        Intent i = new Intent(context, LogInActivity.class);
                        startActivity(i);
                    }

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Log.e("RTA FAIL", "Login con token fallido---------");

            }
        });

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