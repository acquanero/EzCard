package com.acquanero.ezcard.ezdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cards")
public class Tarjeta implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "card_id")
    public int idCard;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "icon")
    public int icon;

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
