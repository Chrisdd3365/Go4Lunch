package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class RestaurantLocation {

    @SerializedName("lat")
    private final Double lat;

    @SerializedName("lng")
    private final Double lng;


    public RestaurantLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantLocation restaurantLocation = (RestaurantLocation) o;
        return Double.compare(restaurantLocation.lat, lat) == 0 &&
                Double.compare(restaurantLocation.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }


    @Override
    public String toString() {
        return "RestaurantLocation{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

}
