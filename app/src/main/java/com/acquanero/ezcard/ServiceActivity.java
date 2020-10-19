package com.acquanero.ezcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class ServiceActivity extends AppCompatActivity {

    SharedPreferences dataDepot;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        toolbar = findViewById(R.id.toolbarService);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_service, menu);
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

        if (id == R.id.menu_item_tarjetas) {
            Intent c = new Intent(getApplicationContext(), CardsActivity.class);
            startActivity(c);
            return true;
        }
        if (id == R.id.item_menu_ezcard) {
            //lanza pantalla ezcard
            return true;
        }
        return super.onOptionsItemSelected(itemConfig);
    }
}