package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class UpdatePinRequest {
    @SerializedName("new_pin")
    int newPin;

    public UpdatePinRequest(int newPin) {
        this.newPin = newPin;
    }
}
