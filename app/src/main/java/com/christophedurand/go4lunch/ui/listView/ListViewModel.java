package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

public class ListViewModel extends AndroidViewModel  {

    private final ListViewRepository mListViewRepository;
    @SuppressWarnings({"FieldCanBeLocal"})
    private LiveData<RestaurantDetailsResponse> mRestaurantDetails = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
        mListViewRepository = ListViewRepository.getInstance();
    }

    public LiveData<RestaurantDetailsResponse> getRestaurantDetailsRepository(String placeId, String apiKey) {
        mRestaurantDetails = loadRestaurantDetailsData(placeId, apiKey);
        return mRestaurantDetails;
    }

    private LiveData<RestaurantDetailsResponse> loadRestaurantDetailsData(String placeId, String apiKey) {
        return mListViewRepository.getRestaurantDetails(placeId, apiKey);
    }
}





