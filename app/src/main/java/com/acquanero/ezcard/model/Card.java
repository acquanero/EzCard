package com.acquanero.ezcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("card_id")
    @Expose
    private Integer cardId;
    @SerializedName("card_name")
    @Expose
    private String cardName;
    @SerializedName("icon")
    @Expose
    private Integer icon;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
