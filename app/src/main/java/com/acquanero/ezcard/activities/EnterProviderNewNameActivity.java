package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.models.Provider;
import com.acquanero.ezcard.models.UserData;
import com.google.gson.Gson;

public class EnterProviderNewNameActivity extends AppCompatActivity {

    private Button buttonAccept, buttonCancel;
    private EditText editProviderName;

    SharedPreferences dataDepot;

    UserData userData;

    int idProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        userData = gson.fromJson(userJson, UserData.class);

        Bundle datos = getIntent().getExtras();
        String nome = datos.getString("providerName");
        idProvider = datos.getInt("providerId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_provider_new_name);

        editProviderName = findViewById(R.id.editProviderName);
        editProviderName.setText(nome);

        buttonAccept = findViewById(R.id.buttonAceptName);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = editProviderName.getText().toString();

                if (isValidProvderName(newName)){

                    Intent goToChangeName = new Intent(getApplicationContext(), EnterPinToConfirmActivity.class);

                    goToChangeName.putExtra("flag","enterPinToChangeProviderName");
                    goToChangeName.putExtra("providerName", newName);
                    goToChangeName.putExtra("providerId", idProvider);

                    startActivity(goToChangeName);

                } else {

                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.invalid_provider_new_name), Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER, 0, 0);
                    t2.show();



                }

            }
        });

        buttonCancel = findViewById(R.id.buttonCancelName);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goBack = new Intent(getApplicationContext(), MainDrawerActivity.class);
                goBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goBack);
                finish();

            }
        });
    }

    //metodo que chequea que el nombre del servicio no sea vacio
    //ni que este repetido
    private boolean isValidProvderName(String nome){

        boolean rta = true;

        if (nome.equalsIgnoreCase("")){
            rta = false;
        }

        for (Provider p : userData.getProviders()){
            if (p.getProviderName().equalsIgnoreCase(nome)){
                rta = false;
            }
        }

        return rta;

    }
}