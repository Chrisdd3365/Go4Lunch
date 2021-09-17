package com.christophedurand.go4lunch.ui.detailsView;


import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Objects;


public class DetailsViewState {

    private final RestaurantDetailsViewState restaurantDetailsViewState;
    private final FirestoreRecyclerOptions<User> workmatesList;


    public DetailsViewState(RestaurantDetailsViewState restaurantDetailsViewState, FirestoreRecyclerOptions<User> workmatesList) {
        this.restaurantDetailsViewState = restaurantDetailsViewState;
        this.workmatesList = workmatesList;
    }


    public RestaurantDetailsViewState getRestaurantDetailsViewState() {
        return restaurantDetailsViewState;
    }

    public FirestoreRecyclerOptions<User> getWorkmatesList() {
        return workmatesList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailsViewState that = (DetailsViewState) o;
        return Objects.equals(restaurantDetailsViewState, that.restaurantDetailsViewState) && Objects.equals(workmatesList, that.workmatesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantDetailsViewState, workmatesList);
    }

    @Override
    public String toString() {
        return "DetailsViewState{" +
                "restaurantDetailsViewState=" + restaurantDetailsViewState +
                ", workmatesList=" + workmatesList +
                '}';
    }

}
