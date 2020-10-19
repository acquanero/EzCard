package com.acquanero.ezcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

public class CardsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        toolbar = findViewById(R.id.toolbarCards);
        setSupportActionBar(toolbar);
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