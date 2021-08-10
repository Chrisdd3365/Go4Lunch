package com.christophedurand.go4lunch.ui.detailsView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class RestaurantDetailsViewModel extends AndroidViewModel {

    private final MediatorLiveData<DetailsViewState> detailsViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<DetailsViewState> getDetailsViewStateLiveData() {
        return detailsViewStateMediatorLiveData;
    }


    public RestaurantDetailsViewModel(@NonNull Application application,
                                      DetailsRepository detailsRepository,
                                      String restaurantId) {
        super(application);

        LiveData<RestaurantDetailsResponse> restaurantDetailsResponseLiveData =
                detailsRepository.getRestaurantDetailsMutableLiveData(restaurantId);

        detailsViewStateMediatorLiveData.addSource(restaurantDetailsResponseLiveData, response ->
                combine(response)
        );

    }

    private void combine(@Nullable RestaurantDetailsResponse response) {

        if (response == null) {
            return;
        }

        RestaurantDetailsViewState restaurantDetailsViewState = new RestaurantDetailsViewState(
                    response.getResult().getPlaceId(),
                    response.getResult().getName(),
                    response.getResult().getFormattedAddress(),
                    response.getResult().getPhotos().get(0).getPhotoReference(),
                    response.getResult().getInternationalPhoneNumber(),
                    response.getResult().getWebsite());

        ArrayList<User> workmatesList = new ArrayList<>();
        DetailsViewState detailsViewState = new DetailsViewState(restaurantDetailsViewState, workmatesList);
        detailsViewStateMediatorLiveData.setValue(detailsViewState);
    }
}
