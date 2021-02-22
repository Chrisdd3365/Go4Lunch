package model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.GooglePlacesAPIService;
import model.RetrofitService;
import model.pojo.NearbyRestaurantsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantsRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<NearbyRestaurantsResponse> mNearbyRestaurantsList = new MutableLiveData<>();
    private static NearbyRestaurantsRepository mNearbyRestaurantsRepository;

    public static NearbyRestaurantsRepository getInstance() {
        if (mNearbyRestaurantsRepository == null){
            mNearbyRestaurantsRepository = new NearbyRestaurantsRepository();
        }
        return mNearbyRestaurantsRepository;
    }

    private NearbyRestaurantsRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

    public LiveData<NearbyRestaurantsResponse> getListOfNearbyRestaurants(String type, String location, String radius, String apiKey) {
        Call<NearbyRestaurantsResponse> nearbyRestaurantsList = mGooglePlacesAPIService.getNearbyPlaces(type, location, radius, apiKey);
        nearbyRestaurantsList.enqueue(new Callback<NearbyRestaurantsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbyRestaurantsResponse> call, @NonNull Response<NearbyRestaurantsResponse> response) {
                mNearbyRestaurantsList.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbyRestaurantsResponse> call, @NonNull Throwable t) { }
        });

        return mNearbyRestaurantsList;
    }

}
