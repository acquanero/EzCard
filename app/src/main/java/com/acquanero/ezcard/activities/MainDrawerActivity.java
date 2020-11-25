package com.acquanero.ezcard.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.models.UserData;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDrawerActivity extends AppCompatActivity {

    private final EzCardApiService myAPIService = ApiUtils.getAPIService();
    private AppBarConfiguration mAppBarConfiguration;
    private String token;
    private int userId;
    SharedPreferences dataDepot;
    NavigationView navigationView;
    SharedPreferences.Editor dataDepotEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);
        userId = dataDepot.getInt("user_id", -1);
        token = dataDepot.getString("token", "");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getUserWholeData(token, userId);
                } catch (Exception e ) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e ) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_cards, R.id.nav_services, R.id.nav_settings).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");
        UserData userData = gson.fromJson(userJson, UserData.class);

        if (userData.getProviders() != null) {
            navController.navigate(R.id.nav_services);
        } else {
            navController.navigate(R.id.nav_cards);
        }

        //Setteo en el header del navView el mail del usuario loggeado
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.labelUserLogged);
        navUsername.setText(userData.getEmail());

        //Le asigno al menu item LogOut la funcion de desloguearse
        MenuItem itemLogOut = navigationView.getMenu().findItem(R.id.nav_logout);
        itemLogOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                showDialog();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    //Descomentar para utilizar menu de arriba a la derecha (el de los tres puntitos)

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.menu_item_logout);

        builder.setMessage(R.string.warning_logout);
        builder.setPositiveButton(R.string.acept_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Vuelvo editable mi SharedPreference
                //y borro toda la info del SharedPreference
                dataDepotEditable = dataDepot.edit();
                dataDepotEditable.clear();
                dataDepotEditable.commit();

                Intent goToLogin = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(goToLogin);

            }
        });
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.show();
    }

    private void getUserWholeData(String token, int userid) throws IOException {
        Call<UserData> callSync = myAPIService.getUserData(AppGeneralUseData.getAppId(), token, userid);
        Response<UserData> response = callSync.execute();

        if (response == null) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, getString(R.string.error_while_getting_user_data), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            Log.e("RTA FAIL", "----Fallo en traer la informacion del usuario------");
        }
        if (response.isSuccessful()) {
            dataDepotEditable = dataDepot.edit();
            UserData user = new UserData();
            user.setName(response.body().getName());
            user.setLastName(response.body().getLastName());
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

        } else {
            Context context = getApplicationContext();
            Toast t = Toast.makeText(context, getString(R.string.error_while_getting_user_data), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }
}

    /*private void getUserWholeData(String token, int userid) {

        myAPIService.getUserData(AppGeneralUseData.getAppId(), token, userid).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                if (response.isSuccessful()) {

                    //Vuelvo editable mi SharedPreference
                    dataDepotEditable = dataDepot.edit();

                    //almaceno los datos del usuario en el sharedPreference

                    UserData user = new UserData();
                    user.setName(response.body().getName());
                    user.setLastName(response.body().getLastName());
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

                } else {
                    Context context = getApplicationContext();
                    Toast t = Toast.makeText(context, getString(R.string.error_while_getting_user_data), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, getString(R.string.error_while_getting_user_data), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                Log.e("RTA FAIL", "----Fallo en traer la informacion del usuario------");

            }
        });
    }
    */
