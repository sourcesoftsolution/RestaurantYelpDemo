package com.example.arunkumar.restaurantapp._model;

/**
 * Created by Arun.Kumar on 3/26/2018.
 */

public class RestModel {
    private String mRestAddress, mRestImage, mRestName, mRestPhoneNo, mRestId;
    private double latitude, longitude;
    private int rating;

    public RestModel(String mRestAddress, String mRestImage, String mRestName,
                     String mRestPhoneNo, String mRestId, double latitude, double longitude, int rating) {
        this.mRestAddress = mRestAddress;
        this.mRestImage = mRestImage;
        this.mRestName = mRestName;
        this.mRestPhoneNo = mRestPhoneNo;
        this.mRestId = mRestId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public RestModel(String mRestImage) {
        this.mRestImage = mRestImage;
    }

    public String getmRestId() {
        return mRestId;
    }

    public void setmRestId(String mRestId) {
        this.mRestId = mRestId;
    }

    public String getmRestName() {
        return mRestName;
    }

    public void setmRestName(String mRestName) {
        this.mRestName = mRestName;
    }

    public String getmRestPhoneNo() {
        return mRestPhoneNo;
    }

    public void setmRestPhoneNo(String mRestPhoneNo) {
        this.mRestPhoneNo = mRestPhoneNo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getmRestAddress() {
        return mRestAddress;
    }

    public void setmRestAddress(String mRestAddress) {
        this.mRestAddress = mRestAddress;
    }

    public String getmRestImage() {
        return mRestImage;
    }

    public void setmRestImage(String mRestImage) {
        this.mRestImage = mRestImage;
    }
}
