package com.acquanero.ezcard.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.acquanero.ezcard.activities.ChangePinActivity;
import com.acquanero.ezcard.R;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class SettingsFragment extends Fragment {

    SharedPreferences dataDepot;

    private String mailSupport = "sebastianreh@gmail.com";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //setteo el mail del usuario en la vista

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);

        TextView textViewMail = root.findViewById(R.id.textViewMail);
        textViewMail.setText(userData.getEmail());

        //linkeo al mail de soporte
        TextView linkAskForHelp = root.findViewById(R.id.linkAskForHelp);
        linkAskForHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("mailto:" + mailSupport);
                Intent sendMail = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(sendMail);

            }
        });

        //ir a la actividad de cambio de PIN
        TextView linkToChangePin = root.findViewById(R.id.linkToChangePin);
        linkToChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToChangePin = new Intent(getActivity(), ChangePinActivity.class);
                startActivity(goToChangePin);

            }
        });

        return root;
    }
}