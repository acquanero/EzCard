package com.acquanero.ezcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class CardsActivity extends AppCompatActivity {

    SharedPreferences dataDepot;
    private Toolbar toolbar;
    private GridLayout gridCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        toolbar = findViewById(R.id.toolbarCards);
        setSupportActionBar(toolbar);

        gridCards = (GridLayout) findViewById(R.id.gridCards);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");

        Gson gson = new Gson();

        UserData u = gson.fromJson(userJson, UserData.class);

        for (Card c : u.getCards()){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(25, 25, 0, 0);
            Button mb = new Button(getApplicationContext());
            mb.setText(c.getName());
            mb.setLayoutParams(params);
            mb.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null));

            gridCards.addView(mb);

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