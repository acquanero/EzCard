package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class BindProviderRequest {
    @SerializedName("card_id")
    int cardId;

    @SerializedName("provider_id")
    int providerId;

    public BindProviderRequest(int cardId, int providerId) {
        this.providerId = providerId;
        this.cardId = cardId;
    }
}
