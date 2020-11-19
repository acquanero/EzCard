package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class DeleteCardRequest {
    @SerializedName("card_id")
    int cardId;


    public DeleteCardRequest(int cardId) {
        this.cardId = cardId;
    }
}
