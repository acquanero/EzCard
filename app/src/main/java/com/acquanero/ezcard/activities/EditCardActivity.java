package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acquanero.ezcard.R;

public class EditCardActivity extends AppCompatActivity {

    private RadioGroup iconGroup;
    private RadioButton botonCard, botonCircle, botonArroba;
    private EditText editTextName;
    private Button cancelButton, aceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        iconGroup = findViewById(R.id.radioGroupIcons);
        botonCard = findViewById(R.id.radioButtonCard);
        botonCircle = findViewById(R.id.radioButtonCircle);
        botonArroba = findViewById(R.id.radioButtonArroba);

        editTextName = findViewById(R.id.editTextCardName);

        Bundle datos = getIntent().getExtras();
        int iconNumber = datos.getInt("cardIcon");
        String cardName = datos.getString("cardName");
        final int cardId = datos.getInt("cardid");

        if (iconNumber == 1) {
            iconGroup.check(R.id.radioButtonCard);
        } else if (iconNumber == 2) {
            iconGroup.check(R.id.radioButtonCircle);
        } else {
            iconGroup.check(R.id.radioButtonArroba);
        }

        editTextName.setText(cardName);

        cancelButton = findViewById(R.id.cancel_edit_card);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goBack = new Intent(getApplicationContext(), MainDrawerActivity.class);
                startActivity(goBack);

            }
        });

        aceptButton = findViewById(R.id.confirm_edit_card);
        aceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextName.getText().toString().equalsIgnoreCase("")){

                    Toast t3 = Toast.makeText(getApplicationContext(), getString(R.string.warning_name_card_notnull) , Toast.LENGTH_LONG);
                    t3.setGravity(Gravity.CENTER,0,0);
                    t3.show();

                } else {

                    Intent i = new Intent(getApplicationContext(), EnterPinToConfirmActivity.class);

                    int cardIconId=0;

                    if(iconGroup.getCheckedRadioButtonId() == R.id.radioButtonCard){
                        cardIconId = 1;
                    } else if (iconGroup.getCheckedRadioButtonId() == R.id.radioButtonCircle){
                        cardIconId = 2;
                    } else {
                        cardIconId = 3;
                    }

                    i.putExtra("flag","enterPinToEditCard");
                    i.putExtra("cardName", editTextName.getText().toString());
                    i.putExtra("cardId", cardId);
                    i.putExtra("cardIcon", cardIconId);

                    startActivity(i);

                }

            }
        });
    }
}