package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddNewCardActivity extends AppCompatActivity {

    Button buttonAceptNewCard, buttonCancelNewCard;
    EditText editNewCardName;
    RadioGroup iconGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        buttonCancelNewCard = findViewById(R.id.button_cancel_New_card);
        buttonCancelNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(i);
            }
        });

        iconGroup = findViewById(R.id.radioGroupIconsNewCard);

        editNewCardName = findViewById(R.id.editTextNewCardName);

        buttonAceptNewCard = findViewById(R.id.button_confirm_New_card);
        buttonAceptNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editNewCardName.getText().toString().equalsIgnoreCase("")){

                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.warning_name_card_notnull) , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();

                } else if (iconGroup.getCheckedRadioButtonId() == -1){

                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_select_icon) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();

                } else {

                    int iconNumber = 0;

                    if (iconGroup.getCheckedRadioButtonId() == R.id.radioButtonCardNew){

                        iconNumber = 1;

                    } else if (iconGroup.getCheckedRadioButtonId() == R.id.radioButtonCircleNew){

                        iconNumber = 2;

                    } else if (iconGroup.getCheckedRadioButtonId() == R.id.radioButtonArrobaNew){

                        iconNumber = 3;
                    }


                    Intent i = new Intent(getApplicationContext(), ReadNewCardActivity.class);

                    i.putExtra("cardName", editNewCardName.getText().toString());
                    i.putExtra("iconNumber", iconNumber);

                    startActivity(i);

                }

            }
        });
    }
}