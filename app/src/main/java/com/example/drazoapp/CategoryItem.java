package com.example.drazoapp;

public class CategoryItem {

    private String ImageLink;
    private String itemDescription;
    private String itemName;
    private String itemPrice;
    private String localisation;

    public CategoryItem() {
    }

    public CategoryItem(String imageLink, String itemDescription, String itemName, String itemPrice, String localisation) {
        this.ImageLink = imageLink;
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.localisation = localisation;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
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

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
