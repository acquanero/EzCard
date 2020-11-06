package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;


public class EditCardActivity extends AppCompatActivity {

    SharedPreferences dataDepot;

    ImageView imageIcono;
    TextView labelTarjeta;
    Button buttonDelete, buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        Bundle datos = getIntent().getExtras();

        final int id = datos.getInt("cardid");
        final String nameCard;
        final int iconId;

        imageIcono = findViewById(R.id.imageCard);
        labelTarjeta = findViewById(R.id.cardName);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonEdit = findViewById(R.id.buttonEdit);


        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        final UserData userData = gson.fromJson(userJson, UserData.class);

        Card cardToEdit = new Card();

        for (Card c : userData.getCards()){

            if (c.getCardId() == id){

                cardToEdit.setIcon(c.getIcon());
                cardToEdit.setName(c.getName());
                cardToEdit.setCardId(c.getCardId());

            }

        }

        nameCard = cardToEdit.getName();
        labelTarjeta.setText(nameCard);

        iconId = cardToEdit.getIcon();

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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteCardActivity = new Intent(getApplicationContext(), EnterPinToConfimActivity.class);
                deleteCardActivity.putExtra("flag", "enterPinToDeleteCard");
                deleteCardActivity.putExtra("cardid", id);
                deleteCardActivity.putExtra("cardName", nameCard);
                startActivity(deleteCardActivity);

            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editCardActivity = new Intent(getApplicationContext(), EditCardStepTwo.class);
                editCardActivity.putExtra("cardid", id);
                editCardActivity.putExtra("cardName", nameCard);
                editCardActivity.putExtra("cardIcon", iconId);
                startActivity(editCardActivity);

            }
        });

    }

}