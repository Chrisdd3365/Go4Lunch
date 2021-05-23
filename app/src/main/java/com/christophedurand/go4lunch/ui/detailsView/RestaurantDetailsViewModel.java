package com.christophedurand.go4lunch.ui.detailsView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;


public class RestaurantDetailsViewModel extends AndroidViewModel {

    private final MediatorLiveData<DetailsUiModel> detailsUiModelMediatorLiveData = new MediatorLiveData<>();
    public LiveData<DetailsUiModel> getDetailsUiModelLiveData() {
        return detailsUiModelMediatorLiveData;
    }


    public RestaurantDetailsViewModel(@NonNull Application application,
                                      DetailsRepository detailsRepository,
                                      String restaurantId) {
        super(application);

        LiveData<RestaurantDetailsResponse> restaurantDetailsResponseLiveData =
                detailsRepository.getRestaurantDetailsMutableLiveData(restaurantId);

        detailsUiModelMediatorLiveData.addSource(restaurantDetailsResponseLiveData, response ->
                combine(response)
        );

    }


    private void combine(@Nullable RestaurantDetailsResponse response) {
        detailsUiModelMediatorLiveData.setValue(
                new DetailsUiModel(
                        response == null ? null : response.result
                )
        );
    }

}
