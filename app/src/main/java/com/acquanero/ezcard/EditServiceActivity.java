package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class EditServiceActivity extends AppCompatActivity {

    SharedPreferences dataDepot;
    private TextView cardNameLabel, serviceTitle;
    private Provider provider;
    private Card associatedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle datos = getIntent().getExtras();
        int idProvider = datos.getInt("idProvider");

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
        setContentView(R.layout.activity_edit_service);

        serviceTitle = findViewById(R.id.labelTitleServiceName);
        serviceTitle.setText(provider.getProviderName());

        for (Card c : userData.getCards()){
            if(c.getCardId() == provider.getCardId()){
                associatedCard = c;
            }
        }

        if (associatedCard != null){
            cardNameLabel = findViewById(R.id.cardNameLabel);
            cardNameLabel.setText(associatedCard.getName());

        } else {
            cardNameLabel.setText(getString(R.string.no_card_associated));
        }

        System.out.println(idProvider);

    }
}