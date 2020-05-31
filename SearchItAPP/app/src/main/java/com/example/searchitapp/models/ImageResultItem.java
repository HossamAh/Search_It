package com.example.searchitapp.models;

import android.os.Parcel;
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

    protected ImageResultItem(Parcel in) {
        ImageURL = in.readString();
        ImageCaption = in.readString();
        ImagePageLink = in.readString();
    }

    public static final Creator<ImageResultItem> CREATOR = new Creator<ImageResultItem>() {
        @Override
        public ImageResultItem createFromParcel(Parcel in) {
            return new ImageResultItem(in);
        }

        @Override
        public ImageResultItem[] newArray(int size) {
            return new ImageResultItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ImageURL);
        parcel.writeString(ImageCaption);
        parcel.writeString(ImagePageLink);
    }
}
