package com.example.coreandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class PlaceLocation implements Parcelable {

    protected PlaceLocation(Parcel in) {
    }

    public static final Creator<PlaceLocation> CREATOR = new Creator<PlaceLocation>() {
        @Override
        public PlaceLocation createFromParcel(Parcel in) {
            return new PlaceLocation(in);
        }

        @Override
        public PlaceLocation[] newArray(int size) {
            return new PlaceLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
