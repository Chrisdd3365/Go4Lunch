package com.christophedurand.go4lunch.ui.detailsView;


import com.christophedurand.go4lunch.model.User;

import java.util.ArrayList;


public class DetailsViewState {

    private final RestaurantDetailsViewState restaurantDetailsViewState;
    private final ArrayList<User> workmatesList;


    public DetailsViewState(RestaurantDetailsViewState restaurantDetailsViewState, ArrayList<User> workmatesList) {
        this.restaurantDetailsViewState = restaurantDetailsViewState;
        this.workmatesList = workmatesList;
    }


    public RestaurantDetailsViewState getRestaurantDetailsViewState() {
        return restaurantDetailsViewState;
    }

    public ArrayList<User> getWorkmatesList() {
        return workmatesList;
    }

}
