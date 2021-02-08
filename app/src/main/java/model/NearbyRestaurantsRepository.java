package model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<Result> mNearbyRestaurantsList = new MutableLiveData<>();
    private static NearbyRestaurantRepository nearbyRestaurantRepository;

    public static NearbyRestaurantRepository getInstance() {
        if (nearbyRestaurantRepository == null){
            nearbyRestaurantRepository = new NearbyRestaurantRepository();
        }
        return nearbyRestaurantRepository;
    }

    public NearbyRestaurantRepository() {
        mGooglePlacesAPIService = RetrofitService.getGooglePlacesAPIService();
    }

    public MutableLiveData<Result> getListOfNearbyRestaurants(String query, String location, String radius, String apiKey) {
        Call<Result> listOfMovieOut = mGooglePlacesAPIService.getNearbyPlaces(query, location, radius, apiKey);
        listOfMovieOut.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                mNearbyRestaurantsList.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                mNearbyRestaurantsList.postValue(null);
            }
        });

        return mNearbyRestaurantsList;
    }
}
