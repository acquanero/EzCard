package com.acquanero.ezcard.ezdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Tarjeta.class, Proveedor.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TarjetaDao tarjetaDao();
    public abstract ProveedorDao proveedorDao();
}
