package com.christophedurand.go4lunch.ui.workmatesView;

import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Objects;


public class WorkmatesViewState {

    private final FirestoreRecyclerOptions<User> workmatesList;


    public WorkmatesViewState(FirestoreRecyclerOptions<User> workmatesList) {
        this.workmatesList = workmatesList;
    }

    public FirestoreRecyclerOptions<User> getWorkmatesList() {
        return workmatesList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewState that = (WorkmatesViewState) o;
        return Objects.equals(workmatesList, that.workmatesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workmatesList);
    }

    @Override
    public String toString() {
        return "WorkmatesViewState{" +
                "workmatesList=" + workmatesList +
                '}';
    }

}
