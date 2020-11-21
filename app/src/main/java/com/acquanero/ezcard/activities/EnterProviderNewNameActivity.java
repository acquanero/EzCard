package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.acquanero.ezcard.R;

public class EnterProviderNewNameActivity extends AppCompatActivity {

    private Button buttonAccept, buttonCancel;
    private EditText editProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle datos = getIntent().getExtras();
        String nome = datos.getString("providerName");
        int idProvider = datos.getInt("providerId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_provider_new_name);

        editProviderName = findViewById(R.id.editProviderName);
        editProviderName.setText(nome);

        buttonAccept = findViewById(R.id.buttonAceptName);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}