package com.christophedurand.go4lunch.ui.detailsView;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class RestaurantDetailsRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private static RestaurantDetailsRepository restaurantDetailsRepository;

    private Map<String, MutableLiveData<RestaurantDetailsResponse>> cache = new HashMap<>();

    public static RestaurantDetailsRepository getInstance() {
        if (restaurantDetailsRepository == null) {
            restaurantDetailsRepository = new RestaurantDetailsRepository();
        }
        return restaurantDetailsRepository;
    }

    private RestaurantDetailsRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

    // TODO merge / split with nearby repo
    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsMutableLiveData(String placeId) {
        MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = cache.get(placeId);
        if (restaurantDetailsMutableLiveData == null) {
            restaurantDetailsMutableLiveData = new MutableLiveData<>();
            // TODO caching à finir
            cache.put(placeId, restaurantDetailsMutableLiveData);

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
        }

        return restaurantDetailsMutableLiveData;
    }

}
