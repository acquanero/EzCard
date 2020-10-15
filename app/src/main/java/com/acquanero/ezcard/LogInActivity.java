
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

import com.acquanero.ezcard.ezdatabase.DataBaseEraser;
import com.acquanero.ezcard.ezdatabase.DataBaseLoader;
import com.acquanero.ezcard.ezdatabase.ModelToSchemaConverter;
import com.acquanero.ezcard.ezdatabase.Proveedor;
import com.acquanero.ezcard.ezdatabase.Tarjeta;
import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInActivity extends AppCompatActivity {

    private EzCardApiService myAPIService;
    private TextView mailUser, password, register;
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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //recupero del layout los botones y los campos de texto
        Button loginButton = (Button) findViewById(R.id.button_login);
        mailUser = (TextView) findViewById(R.id.campo_usuario);
        password = (TextView) findViewById(R.id.campo_password);
        register = (TextView) findViewById(R.id.label_register);

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

                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);

            }
        });
    }

    //metodo a ejecutar al presionar el boton login
    public void logIn(String mail, String passw) {

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

    public void getUserWholeData(String token, int userid) {

        final Context context = this;

        myAPIService.getUserData(generalData.appId, token, userid ).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                //Instancio la clase que me convierte los Models en Schemas para almacenar en la base de dato los Json que recibo
                ModelToSchemaConverter modelToSchema = new ModelToSchemaConverter();

                //Creo 2 listas donde almacenaré los Schemas recibidos luego de la conversion
                ArrayList<Tarjeta> listaTarjeta = new ArrayList<Tarjeta>();
                ArrayList<Proveedor> listaProveedor = new ArrayList<Proveedor>();

                //Vuelvo editable mi SharedPreference
                dataDepotEditable = dataDepot.edit();

                //almaceno los datos del usuario en el sharedPreference
                dataDepotEditable.putString("name", response.body().getName());
                dataDepotEditable.putString("surname", response.body().getSurname());
                dataDepotEditable.putString("password", response.body().getPassword());
                dataDepotEditable.putString("mail", response.body().getMail());
                dataDepotEditable.putString("phone", response.body().getPhone());
                dataDepotEditable.apply();

                //me traigo la lista de tarjetas del usuario, y chequeo si tiene tarjetas asociadas
                //para redireccionar a la activity correspondiente
                List<Card> myCardList = response.body().getCards();
                List<Provider> myProviderList = response.body().getProviders();


                //Recorro las dos lista de Cards y Providers que me devuelve la API y convierto todos los elementos del
                //model de GSON al Schema de room para poder almacenar en la base de datos
                if (myCardList.size() > 0) {
                    for(Card c : myCardList){
                        listaTarjeta.add(modelToSchema.convertCardToTarjeta(c));
                    }
                }

                if(myProviderList.size() > 0){
                    for(Provider p: myProviderList){
                        listaProveedor.add(modelToSchema.convertProviderToProveedor(p));
                    }
                }

                //Limpio todos los datos almacenados en la base de datos, y cargo los nuevos datos traidos del Servidor
                DataBaseEraser dbe = new DataBaseEraser(context);
                DataBaseLoader dbl = new DataBaseLoader(context, listaTarjeta, listaProveedor);

                dbe.start();

                //Utilizo el .join() para asegurarme que el thread que esta borrando la base de datos, finalice antes de empezar a
                //introducir los nuevos datos
                try{
                    dbe.join();
                }catch (InterruptedException ie){}


                dbl.start();

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