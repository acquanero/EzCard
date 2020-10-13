package com.acquanero.ezcard.ezdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProveedorDao {

    //Sentencias SQL para Providers

    @Query("SELECT * FROM providers")
    List<Proveedor> getAllProviders();

    @Insert
    void insertProvider(Proveedor proveedor);

    @Delete
    void deleteProvider(Proveedor proveedor);

    @Update
    void updateProvider(Proveedor proveedor);

    @Query("DELETE FROM providers")
    void deleteAll();
}
