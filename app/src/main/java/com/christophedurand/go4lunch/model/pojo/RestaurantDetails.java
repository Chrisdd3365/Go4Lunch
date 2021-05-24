package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class RestaurantDetails {

    @SerializedName("formatted_address")
    private final String formattedAddress;

    @SerializedName("geometry")
    private final Geometry geometry;

    @SerializedName("icon")
    private final String icon;

    @SerializedName("international_phone_number")
    private final String internationalPhoneNumber;

    @SerializedName("name")
    private final String name;

    @SerializedName("opening_hours")
    private final OpeningHours openingHours;

    @SerializedName("place_id")
    private final String placeId;

    @SerializedName("rating")
    private final Double rating;

    @SerializedName("reference")
    private final String reference;

    @SerializedName("url")
    private final String url;

    @SerializedName("website")
    private final String website;

    @SerializedName("photos")
    private final List<Photo> photos;


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

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public String getPlaceId() {
        return placeId;
    }

    public Double getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public String getUrl() {
        return url;
    }

    public String getWebsite() {
        return website;
    }

    public List<Photo> getPhotos() {
        return photos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetails that = (RestaurantDetails) o;
        return Objects.equals(formattedAddress, that.formattedAddress) &&
                Objects.equals(geometry, that.geometry) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(internationalPhoneNumber, that.internationalPhoneNumber) &&
                Objects.equals(name, that.name) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(rating, that.rating) &&
                Objects.equals(reference, that.reference) &&
                Objects.equals(url, that.url) &&
                Objects.equals(website, that.website) &&
                Objects.equals(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formattedAddress, geometry, icon, internationalPhoneNumber, name, openingHours, placeId, rating, reference, url, website, photos);
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
