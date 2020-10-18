package com.acquanero.ezcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("cellphone")
    @Expose
    private String cellphone;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("cards")
    @Expose
    private List<Card> cards = null;
    @SerializedName("providers")
    @Expose
    private List<Provider> providers = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

}