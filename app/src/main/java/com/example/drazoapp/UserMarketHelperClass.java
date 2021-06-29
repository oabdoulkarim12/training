package com.example.drazoapp;

public class UserMarketHelperClass {

    private String localisation;
    private String itemName;
    private String itemPrice;
    private String itemDescription;
    private String itemState;

    public UserMarketHelperClass() { }

    public UserMarketHelperClass(String localisation, String itemName, String itemPrice, String itemDescription, String itemState) {
        this.localisation = localisation;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemState = itemState;

    }


    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemState() {
        return itemState;
    }

    public void setItemState(String itemState) {
        this.itemState = itemState;
    }
}
