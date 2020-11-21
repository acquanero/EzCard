package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class EditProviderNameRequest {

    @SerializedName("provider_id")
    int providerId;

    @SerializedName("name")
    String name;


    public EditProviderNameRequest(int providerId, String name) {
        this.providerId = providerId;
        this.name = name;
    }
}
