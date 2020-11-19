package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class DeleteProviderRequest {
    @SerializedName("provider_id")
    int providerId;

    public DeleteProviderRequest(int providerId) {
        this.providerId = providerId;
    }
}
