package com.example.searchitapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QueryResultItem implements Parcelable {
    private int relevance;
    private String PageTitle;
    private String PageUrl;
    private String AboutPage;
    private List<String>CategoriesLink;
    private List<String>CategoriesTitle;

    public QueryResultItem(int relevance, String pageTitle, String pageUrl, String aboutPage, List<String> categoriesLink, List<String> categoriesTitle) {
        this.relevance = relevance;
        PageTitle = pageTitle;
        PageUrl = pageUrl;
        AboutPage = aboutPage;
        CategoriesLink = categoriesLink;
        CategoriesTitle = categoriesTitle;
    }

    public QueryResultItem() {
    }

    protected QueryResultItem(Parcel in) {
        relevance = in.readInt();
        PageTitle = in.readString();
        PageUrl = in.readString();
        AboutPage = in.readString();
        CategoriesLink = in.createStringArrayList();
        CategoriesTitle = in.createStringArrayList();
    }

    public static final Creator<QueryResultItem> CREATOR = new Creator<QueryResultItem>() {
        @Override
        public QueryResultItem createFromParcel(Parcel in) {
            return new QueryResultItem(in);
        }

        @Override
        public QueryResultItem[] newArray(int size) {
            return new QueryResultItem[size];
        }
    };

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public String getPageTitle() {
        return PageTitle;
    }

    public void setPageTitle(String pageTitle) {
        PageTitle = pageTitle;
    }

    public String getPageUrl() {
        return PageUrl;
    }

    public void setPageUrl(String pageUrl) {
        PageUrl = pageUrl;
    }

    public String getAboutPage() {
        return AboutPage;
    }

    public void setAboutPage(String aboutPage) {
        AboutPage = aboutPage;
    }

    public List<String> getCategoriesLink() {
        return CategoriesLink;
    }

    public void setCategoriesLink(List<String> categoriesLink) {
        CategoriesLink = categoriesLink;
    }

    public List<String> getCategoriesTitle() {
        return CategoriesTitle;
    }

    public void setCategoriesTitle(List<String> categoriesTitle) {
        CategoriesTitle = categoriesTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(relevance);
        dest.writeString(PageTitle);
        dest.writeString(PageUrl);
        dest.writeString(AboutPage);
        dest.writeStringList(CategoriesLink);
        dest.writeStringList(CategoriesTitle);
    }
}
