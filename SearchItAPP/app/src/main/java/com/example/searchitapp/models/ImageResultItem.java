package com.example.searchitapp.models;

import android.os.Parcelable;

public class ImageResultItem implements Parcelable {
    String ImageURL;
    String ImageCaption;
    String ImagePageLink;


    public ImageResultItem(String imageURL, String imageCaption, String imagePageLink) {
        ImageURL = imageURL;
        ImageCaption = imageCaption;
        ImagePageLink = imagePageLink;
    }

    public ImageResultItem() {
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getImageCaption() {
        return ImageCaption;
    }

    public void setImageCaption(String imageCaption) {
        ImageCaption = imageCaption;
    }

    public String getImagePageLink() {
        return ImagePageLink;
    }

    public void setImagePageLink(String imagePageLink) {
        ImagePageLink = imagePageLink;
    }
}
