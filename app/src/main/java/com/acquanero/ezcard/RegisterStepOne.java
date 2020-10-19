package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.model.UserIdToken;
import com.acquanero.ezcard.myutils.MyValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepOne extends AppCompatActivity {

    private TextView editName, editSurname, editMailAdress, editPhone;
    private Button buttonNext;
    SharedPreferences dataDepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);

        editName = (TextView) findViewById(R.id.editName);
        editSurname = (TextView) findViewById(R.id.editSurname);
        editMailAdress = (TextView) findViewById(R.id.editMailAdress);
        editPhone = (TextView) findViewById(R.id.editPhone);

        buttonNext = (Button) findViewById(R.id.buttonNext);

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
            Intent i = new Intent(getApplicationContext(), RegisterStepTwo.class);

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