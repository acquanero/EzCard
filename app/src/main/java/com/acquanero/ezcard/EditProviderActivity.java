package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class EditProviderActivity extends AppCompatActivity {

    SharedPreferences dataDepot;
    private TextView cardNameLabel, serviceTitle;
    private Provider provider;
    private Card associatedCard;
    private Button buttonChangeAssocCard, buttonDeleteService;
    private int idProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle datos = getIntent().getExtras();
        idProvider = datos.getInt("idProvider");

        associatedCard = null;

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);

        for (Provider p : userData.getProviders()){
            if(p.getProviderId() == idProvider){
                provider = p;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_provider);

        serviceTitle = findViewById(R.id.labelTitleServiceName);
        serviceTitle.setText(provider.getProviderName());

        for (Card c : userData.getCards()){
            if(c.getCardId() == provider.getCardId()){
                associatedCard = c;
            }
        }

        cardNameLabel = findViewById(R.id.cardNameLabel);

        if (associatedCard != null){

            cardNameLabel.setText(associatedCard.getName());

        } else {
            cardNameLabel.setText(getResources().getString(R.string.no_card_associated));
        }

        buttonChangeAssocCard = findViewById(R.id.buttonChangeAssocCard);

        buttonChangeAssocCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSelectCard = new Intent(getApplicationContext(), SelectNewCardForProvider.class);
                goToSelectCard.putExtra("providerId", idProvider);
                startActivity(goToSelectCard);

            }
        });

        buttonDeleteService = findViewById(R.id.buttonDeleteService);
        buttonDeleteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToDelete = new Intent(getApplicationContext(), EnterPinToConfimActivity.class);
                goToDelete.putExtra("flag","enterPinToDeleteProvider");
                goToDelete.putExtra("providerName", provider.getProviderName());
                goToDelete.putExtra("providerId", idProvider);
                startActivity(goToDelete);


            }
        });

    }
}