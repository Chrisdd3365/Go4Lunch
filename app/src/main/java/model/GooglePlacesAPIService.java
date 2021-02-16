package model;


import model.pojo.NearbyRestaurantsResponse;
import model.pojo.RestaurantDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


interface GooglePlacesAPIService {
    @GET("maps/api/place/nearbysearch/json")
    Call<NearbyRestaurantsResponse> getNearbyPlaces(@Query("type") String type,
                                                    @Query("location") String location,
                                                    @Query("radius") String radius,
                                                    @Query("key") String apiKey);

    @GET("maps/api/place/details/json")
    Call<RestaurantDetailsResponse> getPlaceDetails(@Query("place_id") String placeId,
                                                    @Query("key") String apiKey);
}
