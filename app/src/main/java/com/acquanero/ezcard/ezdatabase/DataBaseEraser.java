package com.acquanero.ezcard.ezdatabase;

import android.content.Context;

public class DataBaseEraser extends Thread {

    Context context;

    public DataBaseEraser(Context context) {
        this.context = context;
    }

    public void run(){

        DatabaseClient.getInstance(context).getAppDatabase().tarjetaDao().deleteAll();
        DatabaseClient.getInstance(context).getAppDatabase().proveedorDao().deleteAll();

    }
}
