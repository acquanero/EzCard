package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class EditCardActivity extends AppCompatActivity {

    SharedPreferences dataDepot;

    ImageView imageIcono;

    TextView labelTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        Bundle datos = getIntent().getExtras();

        int id = datos.getInt("cardid");

        imageIcono = (ImageView) findViewById(R.id.imageCard);
        labelTarjeta = (TextView) findViewById(R.id.cardName);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);

        Card cardToEdit = new Card();

        for (Card c : userData.getCards()){

            if (c.getCardId() == id){

                cardToEdit.setIcon(c.getIcon());
                cardToEdit.setName(c.getName());
                cardToEdit.setCardId(c.getCardId());

            }

        }

        labelTarjeta.setText(cardToEdit.getName());

        switch(cardToEdit.getIcon()) {
            case 1:
                imageIcono.setImageResource(R.drawable.ic_card);
                break;
            case 2:
                imageIcono.setImageResource(R.drawable.ic_circle);
                break;
            case 3:
                imageIcono.setImageResource(R.drawable.ic_arroba);
                break;
            default:
                // code block
        }



        System.out.println(id);
    }
}