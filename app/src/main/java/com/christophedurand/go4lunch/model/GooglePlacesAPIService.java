package com.christophedurand.go4lunch.model;


import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GooglePlacesAPIService {
    @GET("maps/api/place/nearbysearch/json")
    Call<NearbyRestaurantsResponse> getNearbyPlaces(@Query("type") String type,
                                                    @Query("location") String location,
                                                    @Query("radius") String radius,
                                                    @Query("key") String apiKey);

    @GET("maps/api/place/details/json")
    Call<RestaurantDetailsResponse> getPlaceDetails(@Query("place_id") String placeId,
                                                    @Query("key") String apiKey);
}
