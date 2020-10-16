package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class ServiceActivity extends AppCompatActivity {

    SharedPreferences dataDepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");

        Gson gson = new Gson();

        UserData u = gson.fromJson(userJson, UserData.class);

        for (Card c : u.getCards()){

            System.out.println(c.getName());

        }
    }
}