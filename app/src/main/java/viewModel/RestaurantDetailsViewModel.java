package viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.repository.RestaurantDetailsRepository;
import model.pojo.RestaurantDetailsResponse;

public class RestaurantDetailsViewModel extends AndroidViewModel  {

    private final RestaurantDetailsRepository mRestaurantDetailsRepository;
    @SuppressWarnings({"FieldCanBeLocal"})
    private LiveData<RestaurantDetailsResponse> mRestaurantDetails = new MutableLiveData<>();

    public RestaurantDetailsViewModel(@NonNull Application application) {
        super(application);
        mRestaurantDetailsRepository = RestaurantDetailsRepository.getInstance();
    }

    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsRepository(String placeId, String apiKey) {
        mRestaurantDetails = loadRestaurantDetailsData(placeId, apiKey);
        return mRestaurantDetails;
    }

    private LiveData<RestaurantDetailsResponse> loadRestaurantDetailsData(String placeId, String apiKey) {
        return mRestaurantDetailsRepository.getRestaurantDetails(placeId, apiKey);
    }
}





