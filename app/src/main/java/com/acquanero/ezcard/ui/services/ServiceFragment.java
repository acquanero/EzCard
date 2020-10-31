package com.acquanero.ezcard.ui.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.acquanero.ezcard.EditCardActivity;
import com.acquanero.ezcard.R;
import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class ServiceFragment extends Fragment {

    SharedPreferences dataDepot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);

        //Guardo el LinearLayout del CardsActivity en una variable
        LinearLayout linearLayoutServices = root.findViewById(R.id.linearLayoutService);

        //Con un for each recorro la lista de tarjetas y genero el imageButton con un label por cada tarjeta
        //y los inserto en un linearLayout horizontal
        //y a su vez este ultimo, lo inserto en cada renglon del linearLayout principal
        for (Provider provider : userData.getProviders()){

            Button botonServicio = new Button(getActivity());
            LinearLayout.LayoutParams botonServicioParams = new LinearLayout.LayoutParams(0, 200);
            botonServicioParams.setMargins(10,10,10,10);
            botonServicioParams.weight = 9f;
            botonServicio.setLayoutParams(botonServicioParams);
            botonServicio.setText(provider.getProviderName());
            botonServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_with_border));

            Button botonEditServicio = new Button(getActivity());
            LinearLayout.LayoutParams botonServicioEditParams = new LinearLayout.LayoutParams(0, 200);
            botonServicioEditParams.setMargins(10,10,10,10);
            botonServicioEditParams.weight = 1f;
            botonEditServicio.setLayoutParams(botonServicioEditParams);
            botonEditServicio.setText("...");
            botonEditServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_with_border));

            LinearLayout lineaHorizontal = new LinearLayout(getContext());
            lineaHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            lineaHorizontal.setGravity(Gravity.CENTER_VERTICAL);


            final int providerId = provider.getProviderId();

            botonEditServicio.setOnClickListener(new View.OnClickListener() {

                Context context = getContext();

                @Override
                public void onClick(View view) {

                }
            });

            botonServicio.setOnClickListener(new View.OnClickListener() {

                Context context = getContext();

                @Override
                public void onClick(View view) {


                }
            });

            lineaHorizontal.addView(botonServicio);
            lineaHorizontal.addView(botonEditServicio);

            linearLayoutServices.addView(lineaHorizontal);
        }
        return root;
    }
}