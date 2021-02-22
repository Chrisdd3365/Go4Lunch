package model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.GooglePlacesAPIService;
import model.RetrofitService;
import model.pojo.RestaurantDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private final MutableLiveData<RestaurantDetailsResponse> mRestaurantDetails = new MutableLiveData<>();
    private static RestaurantDetailsRepository mRestaurantDetailsRepository;

    public static RestaurantDetailsRepository getInstance() {
        if (mRestaurantDetailsRepository == null){
            mRestaurantDetailsRepository = new RestaurantDetailsRepository();
        }
        return mRestaurantDetailsRepository;
    }

    private RestaurantDetailsRepository() {
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

