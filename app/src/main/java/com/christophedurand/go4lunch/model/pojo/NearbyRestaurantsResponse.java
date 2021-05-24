package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


public class NearbyRestaurantsResponse {

    @SerializedName("results")
    private final List<Restaurant> results;


    public NearbyRestaurantsResponse(List<Restaurant> results) {
        this.results = results;
    }


    public List<Restaurant> getResults() {
        return results;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbyRestaurantsResponse that = (NearbyRestaurantsResponse) o;
        return Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public String toString() {
        return "NearbyRestaurantsResponse{" +
                "results=" + results +
                '}';
    }

}
