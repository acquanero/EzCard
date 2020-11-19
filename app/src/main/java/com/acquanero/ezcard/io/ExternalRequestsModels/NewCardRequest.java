package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class NewCardRequest {
    @SerializedName("user_id")
    int userId;

    @SerializedName("serial_number")
    String serialNumber;

    @SerializedName("card_name")
    String cardName;

    @SerializedName("card_icon")
    int cardIcon;

    public NewCardRequest(int userId, String serialNumber, String cardName, int cardIcon) {
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.cardName = cardName;
        this.cardIcon = cardIcon;
    }
}
