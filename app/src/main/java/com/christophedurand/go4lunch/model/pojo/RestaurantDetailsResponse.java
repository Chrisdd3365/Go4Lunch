package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class RestaurantDetailsResponse {

    @SerializedName("result")
    private final RestaurantDetails result;


    public RestaurantDetailsResponse(RestaurantDetails result) {
        this.result = result;
    }


    public RestaurantDetails getResult() {
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsResponse that = (RestaurantDetailsResponse) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }

    @Override
    public String toString() {
        return "RestaurantDetailsResponse{" +
                "result=" + result +
                '}';
    }

}
