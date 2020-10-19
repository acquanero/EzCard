package com.acquanero.ezcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class CardsActivity extends AppCompatActivity {

    SharedPreferences dataDepot;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        toolbar = findViewById(R.id.toolbarCards);
        setSupportActionBar(toolbar);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);


        //Guardo el GridLayout del CardsActivity en una variable
        GridLayout gridCards = (GridLayout) findViewById(R.id.gridCards);

        //Con un for each recorro la lista de tarjetas y genero el imageButton con un label por cada tarjeta
        //y los inserto en un linearLayout vertical
        //y a su vez este ultimo, lo inserto en cada celda del GridLayout
        for (Card card : userData.getCards()){

            LinearLayout linearLayoutInsideGrid = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutInsideGrid.setOrientation(LinearLayout.VERTICAL);
            linearLayoutInsideGrid.setGravity(Gravity.CENTER);
            paramsLinear.setMargins(0, 0, 55, 20);

            Button botonImage = new Button(getApplicationContext());
            //botonImage.setText(card.getName());
            TextView txt = new TextView(getApplicationContext());
            txt.setText(card.getName());
            txt.setGravity(Gravity.CENTER);
            botonImage.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null));

            linearLayoutInsideGrid.addView(botonImage, paramsLinear);
            linearLayoutInsideGrid.addView(txt,paramsLinear);

            gridCards.addView(linearLayoutInsideGrid);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_cards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem itemConfig) {
        //obtengo el id de esa opcion de menu
        int id = itemConfig.getItemId();

        if (id == R.id.item_menu_ajustes) {
            //lanza pantalla ajustes
            return true;
        }

        if (id == R.id.item_menu_servicios) {
            //lanza pantalla de servicios
            return true;
        }
        if (id == R.id.item_menu_ezcard) {
            //lanza pantalla ezcard
            return true;
        }
        return super.onOptionsItemSelected(itemConfig);
    }
}