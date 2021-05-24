package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Restaurant {

    @SerializedName("vicinity")
    private final String vicinity;

    @SerializedName("geometry")
    private final Geometry geometry;

    @SerializedName("icon")
    private final String icon;

    @SerializedName("name")
    private final String name;

    @SerializedName("opening_hours")
    private final OpeningHours openingHours;

    @SerializedName("place_id")
    private final String placeId;

    @SerializedName("rating")
    private final double rating;

    @SerializedName("photos")
    private final List<Photo> photos;


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

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
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

    public double getRating() {
        return rating;
    }

    public List<Photo> getPhotos() {
        return photos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Double.compare(that.rating, rating) == 0 &&
                Objects.equals(vicinity, that.vicinity) &&
                Objects.equals(geometry, that.geometry) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(name, that.name) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vicinity, geometry, icon, name, openingHours, placeId, rating, photos);
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
