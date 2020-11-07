package com.acquanero.ezcard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.R;
import com.acquanero.ezcard.io.ApiUtils;
import com.acquanero.ezcard.io.AppGeneralUseData;
import com.acquanero.ezcard.io.EzCardApiService;
import com.acquanero.ezcard.models.SimpleResponse;
import com.acquanero.ezcard.myutils.NfcTagUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateAccessToProviderActivity extends AppCompatActivity {

    AppGeneralUseData generalData = new AppGeneralUseData();

    SharedPreferences dataDepot;

    private EzCardApiService myAPIService;

    private TextView text;
    private NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private ImageView imageCircle;
    private int idProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_access_to_provider);

        Bundle datos = getIntent().getExtras();
        idProvider = datos.getInt("idProvider");

       //Instancio el sharedPreference
        dataDepot = PreferenceManager.getDefaultSharedPreferences(this);

        //Traigo una instancia de retrofit para realizar los request
        myAPIService = ApiUtils.getAPIService();


        text = findViewById(R.id.labelHoldCardToValidate);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        imageCircle = findViewById(R.id.imageCircleReadCardToValidate);

        if (nfcAdapter == null) {

            Toast toast = Toast.makeText(this, getString(R.string.no_nfc) , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
            finish();
            return;

        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

   @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()){
                showWirelessSettings();
            }

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);

    }

    private void resolveIntent(Intent intent) {

        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            System.out.println("NFC discoveredddd");

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String tagDecimal = getTagInDecimal(tag);
            final String theToken = dataDepot.getString("token", "null");
            final int userId = dataDepot.getInt("user_id", -1);

            imageCircle.setColorFilter(ContextCompat.getColor(this, R.color.acceptedCard), PorterDuff.Mode.MULTIPLY);

            validateProviderWithCard(theToken, userId, idProvider, tagDecimal);

        } else {

            imageCircle.setColorFilter(ContextCompat.getColor(this, R.color.colorDelete), PorterDuff.Mode.MULTIPLY);

        }

    }


    private void showWirelessSettings() {

        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.You_need_to_enable_NFC) , Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();

        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private String getTagInDecimal(Tag tag) {

        byte[] id = tag.getId();
        String theTag = String.valueOf(NfcTagUtils.toDec(id));
        return theTag;

    }

    private void validateProviderWithCard(String token, int userId, int providerId, String tag){

        final String tokenn = token;
        final int iduser = userId;
        final int idProvider = providerId;
        final String tagg = tag;

        final Context context = this;

        myAPIService.entryToProvider(generalData.appId,tokenn,iduser,idProvider,tagg).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {

                    Toast toast = Toast.makeText(context, getString(R.string.successful_access) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                    //Vuelvo a la vista de drawer
                    Intent goToMain = new Intent(context, MainActivity.class);
                    startActivity(goToMain);

                } else {

                    Toast toast = Toast.makeText(context, getString(R.string.validate_provider_error) , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

                Toast toast = Toast.makeText(context, getString(R.string.validate_provider_error) , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

            }
        });

    }
}