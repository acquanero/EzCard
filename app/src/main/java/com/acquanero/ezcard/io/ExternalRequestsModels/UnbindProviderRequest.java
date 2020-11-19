package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class UnbindProviderRequest {
    @SerializedName("provider_id")
    int providerId;

    public UnbindProviderRequest(int providerId) {
        this.providerId = providerId;
    }
}
