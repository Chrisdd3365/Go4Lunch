package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<RestaurantDetailsResponse> mRestaurantDetails = new MutableLiveData<>();
    private static ListViewRepository mListViewRepository;

    public static ListViewRepository getInstance() {
        if (mListViewRepository == null){
            mListViewRepository = new ListViewRepository();
        }
        return mListViewRepository;
    }

    private ListViewRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

    public LiveData<RestaurantDetailsResponse> getRestaurantDetails(String placeId, String apiKey) {
        Call<RestaurantDetailsResponse> restaurantDetails = mGooglePlacesAPIService.getPlaceDetails(placeId, apiKey);
        restaurantDetails.enqueue(new Callback<RestaurantDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetailsResponse> call,
                                   @NonNull Response<RestaurantDetailsResponse> response) {
                mRestaurantDetails.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantDetailsResponse> call, @NonNull Throwable t) { }

            });

        return mRestaurantDetails;
    }

}

