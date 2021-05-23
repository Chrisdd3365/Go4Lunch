package com.christophedurand.go4lunch.ui.listView;


import com.christophedurand.go4lunch.model.pojo.Location;
import com.christophedurand.go4lunch.model.pojo.Photo;

import java.util.List;


class RestaurantViewState {

    private String vicinity;
    private Location location;
    private String icon;
    private String name;
    private String placeId;
    private double rating;
    private List<Photo> photos;
    private OpeningHoursViewState openingHoursViewState;


    public RestaurantViewState(String vicinity,
                               Location location,
                               String icon,
                               String name,
                               String placeId,
                               double rating,
                               List<Photo> photos,
                               OpeningHoursViewState openingHoursViewState) {
        this.vicinity = vicinity;
        this.location = location;
        this.icon = icon;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.photos = photos;
        this.openingHoursViewState = openingHoursViewState;
    }


    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public OpeningHoursViewState getOpeningHoursViewState() {
        return openingHoursViewState;
    }

    public void setOpeningHoursViewState(OpeningHoursViewState openingHoursViewState) {
        this.openingHoursViewState = openingHoursViewState;
    }


    @Override
    public String toString() {
        return "RestaurantViewState{" +
                "vicinity='" + vicinity + '\'' +
                ", location=" + location +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", photos=" + photos +
                ", openingHoursViewState=" + openingHoursViewState +
                '}';
    }

}
