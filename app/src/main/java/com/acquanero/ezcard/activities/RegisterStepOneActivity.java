package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.myutils.MyValidators;


public class RegisterStepOneActivity extends AppCompatActivity {

    private EditText editName, editSurname, editMailAdress, editPhone;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);

        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        editMailAdress = findViewById(R.id.editMailAdress);
        editPhone = findViewById(R.id.editPhone);

        buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToStepTwo();

            }
        });

    }


    //metodo para ir a la segunda pantalla de registro
    private  void goToStepTwo(){
        if (validateFields()){
            Intent i = new Intent(getApplicationContext(), RegisterStepTwoActivity.class);

            i.putExtra("name", editName.getText().toString());
            i.putExtra("lastName", editSurname.getText().toString());
            i.putExtra("mail", editMailAdress.getText().toString());
            i.putExtra("phone", editPhone.getText().toString());

            startActivity(i);
        }

    }

    private boolean validateFields(){

        boolean result = true;

        if (!MyValidators.isOnlyChar(editName.getText().toString())){
            Toast t1 = Toast.makeText(getApplicationContext(), getString(R.string.warning_not_a_word) , Toast.LENGTH_LONG);
            t1.setGravity(Gravity.CENTER,0,0);
            t1.show();
            result = false;
        }

        if (!MyValidators.isOnlyChar(editSurname.getText().toString())){
            Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.warning_not_a_word) , Toast.LENGTH_LONG);
            t2.setGravity(Gravity.CENTER,0,0);
            t2.show();
            result = false;
        }

        if (!MyValidators.isValidEmail(editMailAdress.getText().toString())){
            Toast t3 = Toast.makeText(getApplicationContext(), getString(R.string.warning_invalid_email) , Toast.LENGTH_LONG);
            t3.setGravity(Gravity.CENTER,0,0);
            t3.show();
            result = false;
        }

        if (!MyValidators.isOnlyNumber(editPhone.getText().toString())){
            Toast t4 = Toast.makeText(getApplicationContext(), getString(R.string.warning_not_a_number) , Toast.LENGTH_LONG);
            t4.setGravity(Gravity.CENTER,0,0);
            t4.show();
            result = false;
        }
        
        return result;
    }

}