package com.acquanero.ezcard.io.ExternalRequestsModels;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("name")
    String name;

    @SerializedName("last_name")
    String last_name;

    @SerializedName("password")
    String password;

    @SerializedName("email")
    String mail;

    @SerializedName("cellphone")
    String phone;

    @SerializedName("pin")
    String pin;

    public RegisterRequest(String name, String last_name, String password, String mail, String phone, String pin) {
        this.name = name;
        this.last_name = last_name;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.pin = pin;
    }
}
