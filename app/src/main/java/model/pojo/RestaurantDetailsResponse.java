package model.pojo;

import com.google.gson.annotations.SerializedName;

public class RestaurantDetailsResponse {

    //-- PROPERTIES
    @SerializedName("result")
    public RestaurantDetails result;


    //-- CONSTRUCTOR
    public RestaurantDetailsResponse(RestaurantDetails result) {
        this.result = result;
    }


    //-- GETTERS && SETTERS
    public RestaurantDetails getResult() {
        return result;
    }

    public void setResult(RestaurantDetails result) {
        this.result = result;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "RestaurantDetailsResponse{" +
                "result=" + result +
                '}';
    }

}
