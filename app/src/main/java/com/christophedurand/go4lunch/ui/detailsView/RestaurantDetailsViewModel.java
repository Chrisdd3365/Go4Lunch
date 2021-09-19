package com.christophedurand.go4lunch.ui.detailsView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.ui.workmatesView.User;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class RestaurantDetailsViewModel extends AndroidViewModel {

    private final MediatorLiveData<DetailsViewState> detailsViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<DetailsViewState> getDetailsViewStateLiveData() {
        return detailsViewStateMediatorLiveData;
    }

    private final LifecycleOwner owner;


    public RestaurantDetailsViewModel(@NonNull Application application,
                                      DetailsRepository detailsRepository,
                                      String restaurantId,
                                      LifecycleOwner owner) {
        super(application);

        this.owner = owner;

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
                    response.getResult().getWebsite()
        );

        FirestoreRecyclerOptions<User> workmatesList = createWorkmatesFilteredDataSource(response.getResult().getPlaceId());
        DetailsViewState detailsViewState = new DetailsViewState(restaurantDetailsViewState, workmatesList);
        detailsViewStateMediatorLiveData.setValue(detailsViewState);
    }

    private FirestoreRecyclerOptions<User> createWorkmatesFilteredDataSource(String restaurantId) {
        Query query = UserManager.getInstance().getUsersFilteredListBy(restaurantId);
        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).setLifecycleOwner(owner).build();
    }

    public void onCleared() {
        detailsViewStateMediatorLiveData.removeObservers(owner);
    }

}
