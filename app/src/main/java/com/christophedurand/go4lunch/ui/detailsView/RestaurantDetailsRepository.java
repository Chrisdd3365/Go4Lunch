package com.christophedurand.go4lunch.ui.detailsView;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class RestaurantDetailsRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = new MutableLiveData<>();
    private static RestaurantDetailsRepository restaurantDetailsRepository;


    public static RestaurantDetailsRepository getInstance() {
        if (restaurantDetailsRepository == null) {
            restaurantDetailsRepository = new RestaurantDetailsRepository();
        }
        return restaurantDetailsRepository;
    }

    private RestaurantDetailsRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }


    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsMutableLiveData(String placeId) {
        Call<RestaurantDetailsResponse> restaurantDetails = mGooglePlacesAPIService.getPlaceDetails(placeId, apiKey);
        restaurantDetails.enqueue(new Callback<RestaurantDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetailsResponse> call,
                                   @NonNull Response<RestaurantDetailsResponse> response) {
                restaurantDetailsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantDetailsResponse> call, @NonNull Throwable t) { }

        });

        return restaurantDetailsMutableLiveData;
    }

}
