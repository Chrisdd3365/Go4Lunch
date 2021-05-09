package com.christophedurand.go4lunch.ui.mapView;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapViewRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseMutableLiveData = new MutableLiveData<>();
    private static MapViewRepository mMapViewRepository;

    public static MapViewRepository getInstance() {
        if (mMapViewRepository == null){
            mMapViewRepository = new MapViewRepository();
        }
        return mMapViewRepository;
    }

    private MapViewRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

    public LiveData<NearbyRestaurantsResponse> getNearbyRestaurantsResponseLiveData(String type, String location, String radius, String apiKey) {
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

}
