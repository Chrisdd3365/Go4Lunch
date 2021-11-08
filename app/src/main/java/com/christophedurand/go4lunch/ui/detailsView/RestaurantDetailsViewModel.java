package com.christophedurand.go4lunch.ui.detailsView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class RestaurantDetailsViewModel extends AndroidViewModel {

    private final MediatorLiveData<DetailsViewState> detailsViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<DetailsViewState> getDetailsViewStateLiveData() {
        return detailsViewStateMediatorLiveData;
    }



    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final String restaurantId;


    public RestaurantDetailsViewModel(@NonNull Application application,
                                      DetailsRepository detailsRepository,
                                      UserRepository userRepository,
                                      FirebaseAuth firebaseAuth,
                                      String restaurantId) {
        super(application);

        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;
        this.restaurantId = restaurantId;

        LiveData<RestaurantDetailsResponse> restaurantDetailsResponseLiveData = detailsRepository.getRestaurantDetailsMutableLiveData(restaurantId);

        LiveData<List<User>> usersFilteredListLiveData = userRepository.fetchUsersFilteredList(restaurantId);

        LiveData<Boolean> joiningMutableLiveData = userRepository.getIsJoiningLiveData();

        LiveData<Boolean> favoriteMutableLiveData= userRepository.getIsFavoriteLiveData();

        detailsViewStateMediatorLiveData.addSource(restaurantDetailsResponseLiveData, response ->
                combine(response, joiningMutableLiveData.getValue(), favoriteMutableLiveData.getValue(), usersFilteredListLiveData.getValue())
        );

        detailsViewStateMediatorLiveData.addSource(joiningMutableLiveData, joining ->
                combine(restaurantDetailsResponseLiveData.getValue(), joining, favoriteMutableLiveData.getValue(), usersFilteredListLiveData.getValue())
        );

        detailsViewStateMediatorLiveData.addSource(favoriteMutableLiveData, favorite ->
                combine(restaurantDetailsResponseLiveData.getValue(), joiningMutableLiveData.getValue(), favorite, usersFilteredListLiveData.getValue())
            );

        detailsViewStateMediatorLiveData.addSource(usersFilteredListLiveData, usersFilteredList ->
                 combine(restaurantDetailsResponseLiveData.getValue(), joiningMutableLiveData.getValue(), favoriteMutableLiveData.getValue(), usersFilteredList)
        );

    }

    private void combine(@Nullable RestaurantDetailsResponse response,
                         @Nullable Boolean joiningButtonState,
                         @Nullable Boolean favoriteButtonState,
                         List<User> usersFilteredList) {

        if (response == null) {
            return;
        }

        detailsViewStateMediatorLiveData.setValue(
                new DetailsViewState(
                        response.getResult().getPlaceId(),
                        response.getResult().getName(),
                        response.getResult().getFormattedAddress(),
                        response.getResult().getPhotos().get(0).getPhotoReference(),
                        response.getResult().getInternationalPhoneNumber(),
                        response.getResult().getWebsite(),
                        joiningButtonState != null && joiningButtonState ? R.drawable.ic_check_circle_green : R.drawable.ic_check_circle_red,
                        favoriteButtonState != null && favoriteButtonState ? R.drawable.ic_star_filled : R.drawable.ic_star_outline,
                        usersFilteredList
                )
        );

    }

    public void setJoiningButtonState() {
        userRepository.setCurrentUserIsJoining(restaurantId, "", "");
    }

    public void setFavoriteButtonState() {
        userRepository.setCurrentUserIsFavorite(restaurantId);
    }

}
