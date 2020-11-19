package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class EditCardRequest {
    @SerializedName("card_id")
    int cardId;

    @SerializedName("card_name")
    String cardName;

    @SerializedName("card_icon")
    int cardIcon;

    public EditCardRequest(int cardId, String cardName, int cardIcon) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardIcon = cardIcon;
    }
}
