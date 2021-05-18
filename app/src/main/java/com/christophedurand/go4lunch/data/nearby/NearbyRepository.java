package com.christophedurand.go4lunch.data.nearby;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class NearbyRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private static NearbyRepository sNearbyRepository;

    public static NearbyRepository getInstance() {
        if (sNearbyRepository == null){
            sNearbyRepository = new NearbyRepository();
        }
        return sNearbyRepository;
    }

    private NearbyRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

    public LiveData<NearbyRestaurantsResponse> getNearbyRestaurantsResponseByRadiusLiveData(String type, String location, String radius, String apiKey) {
        final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseMutableLiveData = new MutableLiveData<>();
        Call<NearbyRestaurantsResponse> nearbyRestaurantsList = mGooglePlacesAPIService.getNearbyPlaces(type, location, radius, apiKey);
        nearbyRestaurantsList.enqueue(new Callback<NearbyRestaurantsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbyRestaurantsResponse> call, @NonNull Response<NearbyRestaurantsResponse> response) {
                nearbyRestaurantsResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbyRestaurantsResponse> call, @NonNull Throwable t) { }
        });

        return nearbyRestaurantsResponseMutableLiveData;
    }

    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsMutableLiveData(String placeId) {
        final MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = new MutableLiveData<>();

        Call<RestaurantDetailsResponse> restaurantDetails = mGooglePlacesAPIService.getPlaceDetails(placeId, apiKey);
        restaurantDetails.enqueue(new Callback<RestaurantDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetailsResponse> call,
                                   @NonNull Response<RestaurantDetailsResponse> response) {
                restaurantDetailsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantDetailsResponse> call, @NonNull Throwable t) {
                Log.d("onFailure called", "Restaurant details  response is null");
            }

        });

        return restaurantDetailsMutableLiveData;
    }
}
