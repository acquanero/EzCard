package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.myutils.MyValidators;

public class EnterPinToEditCard extends AppCompatActivity {

    AppGeneralUseData generalData = new AppGeneralUseData();

    private Button buttonEditConfirm, buttonCanel;
    private TextView editPIN;

    private int pinMin = 4;
    private int pinMax = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_to_edit_card);

        Bundle datos = getIntent().getExtras();

        String nameCard = datos.getString("cardName");
        int cardId = datos.getInt("cardId");
        int cardIconNumber = datos.getInt("cardIcon");

        System.out.println("nombre: " + nameCard);
        System.out.println("card id: " + cardId);
        System.out.println("icon: " + cardIconNumber);

        editPIN = (TextView) findViewById(R.id.inputPin);

        buttonCanel = (Button) findViewById(R.id.cancelEditButton);
        buttonCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), MainDrawer.class);
                startActivity(goBack);
            }
        });

        buttonEditConfirm = (Button) findViewById(R.id.confirmEditButton);
        buttonEditConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int pinEntered = Integer.parseInt(editPIN.getText().toString());
                String pinEnteredInString = editPIN.getText().toString();

                if (!MyValidators.isBetween(pinEnteredInString,pinMin,pinMax) || !MyValidators.isOnlyNumber(pinEnteredInString)){
                    Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_pin) , Toast.LENGTH_LONG);
                    t2.setGravity(Gravity.CENTER,0,0);
                    t2.show();
                } else {
                    //sendConfirmEditCard(theToken, pinEntered, userID, idCard, cardName, cardIcon);
                }
            }
        });
    }

    public void sendConfirmEditCard(String token, int pin, int userid, int cardid, String cardName, int cardIcon){

    }
}