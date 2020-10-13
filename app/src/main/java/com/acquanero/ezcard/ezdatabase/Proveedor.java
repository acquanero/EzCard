package com.acquanero.ezcard.ezdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "providers")
public class Proveedor implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "provider_id")
    public int idProvider;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "cardId")
    public int cardId;

    @ColumnInfo(name = "enabled")
    public Boolean enabled;

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
