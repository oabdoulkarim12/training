package com.example.drazoapp;

import java.util.ArrayList;
import java.util.List;

public class ImageHelperClass {

    private List<String> urls = new ArrayList<>();

    public ImageHelperClass() {
    }

    public ImageHelperClass(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void addElement(String newElement)
    {
        urls.add(newElement);
    }
}
