package model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NearbyRestaurantResponse {

    //-- PROPERTIES
    @SerializedName("results")
    public List<Restaurant> results;


    //-- CONSTRUCTOR
    public NearbyRestaurantResponse(List<Restaurant> results) {
        this.results = results;
    }


    //-- GETTERS && SETTERS
    public List<Restaurant> getResults() {
        return results;
    }

    public void setResults(List<Restaurant> results) {
        this.results = results;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "NearbyRestaurantResponse{" +
                "results=" + results +
                '}';
    }

}
