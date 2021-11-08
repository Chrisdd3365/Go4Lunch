package com.christophedurand.go4lunch.data.details;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.apiKey;


public class DetailsRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private static DetailsRepository sDetailsRepository;

    private Map<String, RestaurantDetailsResponse> cache = new HashMap<>();

    public static DetailsRepository getInstance() {
        if (sDetailsRepository == null) {
            sDetailsRepository = new DetailsRepository();
        }
        return sDetailsRepository;
    }

    private DetailsRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }


    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsMutableLiveData(String placeId) {
        MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = new MutableLiveData<>();
        RestaurantDetailsResponse restaurantDetailsResponse = cache.get(placeId);
        if (restaurantDetailsResponse != null) {
            restaurantDetailsMutableLiveData.setValue(restaurantDetailsResponse);
        } else {
            Call<RestaurantDetailsResponse> restaurantDetails = mGooglePlacesAPIService.getPlaceDetails(placeId, apiKey);
            restaurantDetails.enqueue(new Callback<RestaurantDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<RestaurantDetailsResponse> call,
                                       @NonNull Response<RestaurantDetailsResponse> response) {
                    if (response.body() != null) {
                        cache.put(placeId, response.body());
                        restaurantDetailsMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RestaurantDetailsResponse> call, @NonNull Throwable t) {
                    Log.d("onFailure called", "Restaurant details  response is null");
                }
            });
        }

        return restaurantDetailsMutableLiveData;
    }

}
