package com.acquanero.ezcard.ezdatabase;

import android.content.Context;
import java.util.ArrayList;


public class DataBaseLoader extends Thread {

    Context context;
    ArrayList<Tarjeta> cardList;
    ArrayList<Proveedor> providerList;

    public DataBaseLoader(Context context, ArrayList<Tarjeta> cardList, ArrayList<Proveedor> providerList) {
        this.context = context;
        this.cardList = cardList;
        this.providerList = providerList;
    }

    public void run(){

        for (Tarjeta t: cardList){
            DatabaseClient.getInstance(context).getAppDatabase().tarjetaDao().insertCard(t);
        }

        for (Proveedor p: providerList){
            DatabaseClient.getInstance(context).getAppDatabase().proveedorDao().insertProvider(p);
        }

    }

}
