package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class UpdatePinRequest {
    @SerializedName("new_pin")
    String newPin;

    public UpdatePinRequest(String newPin) {
        this.newPin = newPin;
    }
}
