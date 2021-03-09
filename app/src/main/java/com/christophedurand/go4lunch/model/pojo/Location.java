package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Location {

    //-- PROPERTIES
    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;


    //-- CONSTRUCTOR
    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    //-- GETTERS && SETTERS
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

}
