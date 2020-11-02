package com.acquanero.ezcard.ui.providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.acquanero.ezcard.EditProviderActivity;
import com.acquanero.ezcard.R;
import com.acquanero.ezcard.ValidateAccessToProviderActivity;
import com.acquanero.ezcard.model.Provider;
import com.acquanero.ezcard.model.UserData;
import com.google.gson.Gson;

public class ProvidersFragment extends Fragment {

    SharedPreferences dataDepot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_providers, container, false);

        //Creo una instancia de SahredPreference para almacenar informacion
        //el archivo se encuentra en /data/data/[nombre del proyecto]/shared_prefs/archivo.xml
        dataDepot = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String userJson = dataDepot.getString("usuario", "null");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJson, UserData.class);

        //Guardo el LinearLayout del CardsActivity en una variable
        LinearLayout linearLayoutServices = root.findViewById(R.id.linearLayoutService);

        //Con un for each recorro la lista de Servicios y genero el button por cada servicio y un button para editar servicio
        //y los inserto en un linearLayout horizontal
        //y a su vez este ultimo, lo inserto en cada renglon del linearLayout principal
        for (final Provider provider : userData.getProviders()){

            Button botonServicio = new Button(getActivity());
            LinearLayout.LayoutParams botonServicioParams = new LinearLayout.LayoutParams(0, 200);
            botonServicioParams.setMargins(10,10,10,10);
            botonServicioParams.weight = 9f;
            botonServicio.setLayoutParams(botonServicioParams);
            botonServicio.setText(provider.getProviderName());

            final int providerId = provider.getProviderId();

            //Si el servicio esta deshabilitado, pinto el boton de gris y le asocio un toast al click que muestra advertencia
            //de lo contrario lo pinto de blanco y asocio el listener a la siguiente actividad para leer la tarjeta
            if (provider.getEnabled() == false){

                botonServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_wborder_disabled));

                botonServicio.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Toast t = Toast.makeText(getActivity(), getString(R.string.service_unavailable) , Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();

                    }
                });

            }  else {

                botonServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_with_border));
                botonServicio.setOnClickListener(new View.OnClickListener() {

                    Provider theProvider = provider;

                    @Override
                    public void onClick(View view) {


                        //Al hacer click para entrar al servicio, chequeo primero que tenga tarjetas asociadas
                        if (theProvider.getCardId() == null){

                            Toast t = Toast.makeText(getActivity(), getString(R.string.no_card_binded_to_service) , Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER,0,0);
                            t.show();

                        } else {

                            Intent validateAccess = new Intent(getActivity(), ValidateAccessToProviderActivity.class);
                            validateAccess.putExtra("idProvider", providerId);
                            startActivity(validateAccess);

                        }

                    }
                });

            }

            Button botonEditServicio = new Button(getActivity());
            LinearLayout.LayoutParams botonServicioEditParams = new LinearLayout.LayoutParams(0, 200);
            botonServicioEditParams.setMargins(10,10,10,10);
            botonServicioEditParams.weight = 1f;
            botonEditServicio.setLayoutParams(botonServicioEditParams);
            botonEditServicio.setText("...");

            if (provider.getEnabled() == false){
                botonEditServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_wborder_disabled));
            } else botonEditServicio.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_with_border));

            LinearLayout lineaHorizontal = new LinearLayout(getContext());
            lineaHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            lineaHorizontal.setGravity(Gravity.CENTER_VERTICAL);


            botonEditServicio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getActivity(), EditProviderActivity.class);
                    i.putExtra("idProvider", providerId);
                    startActivity(i);

                }
            });

            lineaHorizontal.addView(botonServicio);
            lineaHorizontal.addView(botonEditServicio);

            linearLayoutServices.addView(lineaHorizontal);
        }
        return root;
    }
}