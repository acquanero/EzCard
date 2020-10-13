package com.acquanero.ezcard.ezdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TarjetaDao {

    //Sentencias SQL para Cards

    @Query("SELECT * FROM cards")
    List<Tarjeta> getAllCards();

    @Insert
    void insertCard(Tarjeta tarjeta);

    @Delete
    void deleteCard(Tarjeta tarjeta);

    @Update
    void updateCard(Tarjeta tarjeta);

    @Query("DELETE FROM cards")
    void deleteAll();

}
