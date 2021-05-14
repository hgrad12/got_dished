package com.example.gotdished.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private int number;
    private String details;

    public Step(int number, String details) {
        this.number = number;
        this.details = details;
    }

    protected Step(Parcel in) {
        number = in.readInt();
        details = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Step () {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(details);
    }
}
