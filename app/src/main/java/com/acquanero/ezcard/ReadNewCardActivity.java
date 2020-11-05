package com.acquanero.ezcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acquanero.ezcard.myutils.NfcTagUtils;


public class ReadNewCardActivity extends AppCompatActivity {

    private TextView text;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private String cardName;
    private int iconNumber;

    private ImageView imageCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_new_card);
        text = findViewById(R.id.labelHoldCard);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Bundle datos = getIntent().getExtras();

        cardName = datos.getString("cardName");
        iconNumber = datos.getInt("iconNumber");

        imageCircle = findViewById(R.id.imageCircleReadCard);


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
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

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

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String tagDecimal = getTagInDecimal(tag);

            imageCircle.setColorFilter(ContextCompat.getColor(this, R.color.acceptedCard), android.graphics.PorterDuff.Mode.MULTIPLY);

            goToEnterPin(tagDecimal);

        } else {

            imageCircle.setColorFilter(ContextCompat.getColor(this, R.color.colorDelete), android.graphics.PorterDuff.Mode.MULTIPLY);

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

    private void goToEnterPin(String tag){

        Intent i = new Intent(getApplicationContext(), EnterPinToConfimActivity.class);

        i.putExtra("flag", "enterPinToAddNewCard");
        i.putExtra("nameCard", cardName);
        i.putExtra("iconNumber", iconNumber);
        i.putExtra("tag", tag);

        startActivity(i);

    }
}