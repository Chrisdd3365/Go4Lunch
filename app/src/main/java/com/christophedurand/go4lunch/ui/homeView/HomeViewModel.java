package com.christophedurand.go4lunch.ui.homeView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;


public class HomeViewModel extends AndroidViewModel {

    private final MediatorLiveData<HomeViewState> homeViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<HomeViewState> getHomeViewStateMediatorLiveData() {
        return homeViewStateMediatorLiveData;
    }

    private final UserRepository _userRepository;


    public HomeViewModel(@NonNull Application application,
                         UserRepository userRepository) {
        super(application);

        _userRepository = userRepository;

        LiveData<User> currentUserLiveData = _userRepository.getCurrentUserLiveData();

        LiveData<String> chosenRestaurantIdLiveData = _userRepository.getRestaurantIdLiveData();


        homeViewStateMediatorLiveData.addSource(currentUserLiveData, currentUser -> {
            combine(currentUser, chosenRestaurantIdLiveData.getValue());
        });

        homeViewStateMediatorLiveData.addSource(chosenRestaurantIdLiveData, chosenRestaurantId -> {
            combine(currentUserLiveData.getValue(), chosenRestaurantId);
        });

    }


    private void combine(@Nullable User currentUser,
                         String chosenRestaurantId) {
        homeViewStateMediatorLiveData.setValue(
                new HomeViewState(
                        currentUser,
                        chosenRestaurantId
                )
        );
    }

    public void getUpdatedRestaurantId() {
        _userRepository.getUpdatedChosenRestaurantId();
    }

}
