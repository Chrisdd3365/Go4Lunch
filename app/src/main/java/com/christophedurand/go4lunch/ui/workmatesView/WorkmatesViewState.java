package com.christophedurand.go4lunch.ui.workmatesView;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.User;

import java.util.List;


public class WorkmatesViewState {

    @Nullable
    private final List<User> workmatesList;


    public WorkmatesViewState(@Nullable List<User> workmatesList) {
        this.workmatesList = workmatesList;
    }


    @Nullable
    public List<User> getWorkmatesList() {
        return workmatesList;
    }

}
