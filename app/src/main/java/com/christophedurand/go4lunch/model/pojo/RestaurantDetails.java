package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantDetails {

    //-- PROPERTIES
    @SerializedName("formatted_address")
    public String formattedAddress;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("international_phone_number")
    public String internationalPhoneNumber;

    @SerializedName("name")
    public String name;

    @SerializedName("opening_hours")
    public OpeningHours openingHours;

    @SerializedName("place_id")
    public String placeId;

    @SerializedName("rating")
    public double rating;

    @SerializedName("reference")
    public String reference;

    @SerializedName("url")
    public String url;

    @SerializedName("website")
    public String website;

    @SerializedName("photos")
    public List<Photo> photos;


    public RestaurantDetails(String formattedAddress, Geometry geometry,
                             String icon, String internationalPhoneNumber,
                             String name, OpeningHours openingHours,
                             String placeId, double rating,
                             String reference, String url,
                             String website, List<Photo> photos) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.icon = icon;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.openingHours = openingHours;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.url = url;
        this.website = website;
        this.photos = photos;
    }


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

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }


    @Override
    public String toString() {
        return "RestaurantDetails{" +
                "formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", internationalPhoneNumber='" + internationalPhoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", reference='" + reference + '\'' +
                ", url='" + url + '\'' +
                ", website='" + website + '\'' +
                ", photos=" + photos +
                '}';
    }

}
