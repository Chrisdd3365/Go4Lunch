package com.christophedurand.go4lunch.ui.workmatesView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class WorkmatesViewModel extends AndroidViewModel {

    private final MediatorLiveData<WorkmatesViewState> workmatesViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<WorkmatesViewState> getWorkmatesViewStateMediatorLiveData() {
        return workmatesViewStateMediatorLiveData;
    }


    public WorkmatesViewModel(@NonNull Application application,
                              UserRepository userRepository,
                              FirebaseAuth firebaseAuth) {
        super(application);

        LiveData<List<User>> allUsersLiveData = userRepository.fetchWorkmatesList(firebaseAuth.getUid());

        workmatesViewStateMediatorLiveData.addSource(allUsersLiveData, allUsers -> {
            combine(allUsers);
        });

    }


    private void combine(@Nullable List<User> workmatesList) {
        workmatesViewStateMediatorLiveData.setValue(
                new WorkmatesViewState(
                        workmatesList
                )
        );
    }

}