package com.christophedurand.go4lunch.ui.homeView;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.User;


public class HomeViewState {

    @Nullable
    private final User currentUser;
    private final String chosenRestaurantId;


    public HomeViewState(@Nullable User currentUser, String chosenRestaurantId) {
        this.currentUser = currentUser;
        this.chosenRestaurantId = chosenRestaurantId;
    }


    @Nullable
    public User getCurrentUser() {
        return currentUser;
    }

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

}
