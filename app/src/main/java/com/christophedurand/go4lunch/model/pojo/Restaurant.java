package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

    //-- PROPERTIES
    @SerializedName("formatted_address")
    public String formattedAddress;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("name")
    public String name;

    @SerializedName("opening_hours")
    public OpeningHours openingHours;

    @SerializedName("place_id")
    public String placeId;

    @SerializedName("rating")
    public double rating;


    //-- CONSTRUCTOR
    public Restaurant(String formattedAddress, Geometry geometry, String icon, String name,
                      OpeningHours openingHours, String placeId, double rating) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.icon = icon;
        this.name = name;
        this.openingHours = openingHours;
        this.placeId = placeId;
        this.rating = rating;
    }


    //-- GETTERS && SETTERS
    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "Restaurant{" +
                "formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
