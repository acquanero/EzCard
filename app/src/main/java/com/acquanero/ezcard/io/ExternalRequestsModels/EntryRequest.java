package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class EntryRequest {
    @SerializedName("user_id")
    int userId;

    @SerializedName("serial_number")
    String serialNumber;

    public EntryRequest(int userId, String serialNumber) {
        this.userId = userId;
        this.serialNumber = serialNumber;
    }
}
