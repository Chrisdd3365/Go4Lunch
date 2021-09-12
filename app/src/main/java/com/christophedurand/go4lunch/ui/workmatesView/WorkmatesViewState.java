package com.christophedurand.go4lunch.ui.workmatesView;

import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class WorkmatesViewState {

    private final FirestoreRecyclerOptions<User> workmatesList;


    public WorkmatesViewState(FirestoreRecyclerOptions<User> workmatesList) {
        this.workmatesList = workmatesList;
    }


    public FirestoreRecyclerOptions<User> getWorkmatesList() {
        return workmatesList;
    }

}
