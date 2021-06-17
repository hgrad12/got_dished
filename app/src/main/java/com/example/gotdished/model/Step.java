package com.example.gotdished.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Step implements Serializable {
    private String details;

    public Step(String details) {
        this.details = details;
    }

    protected Step(Parcel in) {
        details = in.readString();
    }

//    public static final Creator<Step> CREATOR = new Creator<Step>() {
//        @Override
//        public Step createFromParcel(Parcel in) {
//            return new Step(in);
//        }
//
//        @Override
//        public Step[] newArray(int size) {
//            return new Step[size];
//        }
//    };

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Step () {}

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(details);
//    }
}
