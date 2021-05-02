package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {

    @SerializedName("vicinity")
    public String vicinity;

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

    @SerializedName("photos")
    private List<Photo> photos;


    public Restaurant(String vicinity, Geometry geometry, String icon, String name,
                      OpeningHours openingHours, String placeId, double rating, List<Photo> photos) {
        this.vicinity = vicinity;
        this.geometry = geometry;
        this.icon = icon;
        this.name = name;
        this.openingHours = openingHours;
        this.placeId = placeId;
        this.rating = rating;
        this.photos = photos;
    }


    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
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

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                "vicinity='" + vicinity + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", photos=" + photos +
                '}';
    }

}
