package com.codepath.android.booksearch.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.codepath.android.booksearch.utils.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book implements Parcelable{

    @SerializedName("edition_key")
    private List<String> editionKeys;

    @SerializedName("cover_edition_key")
    private String coverEditionKey;

    @SerializedName("author_name")
    private List<String> authors;

    @SerializedName("title_suggest")
    private String title;

    protected Book(Parcel in) {
        editionKeys = in.createStringArrayList();
        coverEditionKey = in.readString();
        authors = in.createStringArrayList();
        title = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getOpenLibraryId() {
        if (editionKeys != null && editionKeys.size() > 0) {
            return editionKeys.get(0);
        }
        return coverEditionKey;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        if (authors != null) {
            return TextUtils.join(", ", authors);
        }
        return "No author";
    }

    public String getCoverUrl() {
        return Constant.STATIC_BASE_URL + getOpenLibraryId() + "-L.jpg?default=false";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(editionKeys);
        dest.writeString(coverEditionKey);
        dest.writeStringList(authors);
        dest.writeString(title);
    }
}
