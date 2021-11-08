package com.christophedurand.go4lunch.ui.homeView;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.User;


public class HomeViewState {

    @Nullable
    private final User currentUser;


    public HomeViewState(@Nullable User currentUser) {
        this.currentUser = currentUser;
    }


    @Nullable
    public User getCurrentUser() {
        return currentUser;
    }

}
