package com.christophedurand.go4lunch.ui.homeView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;


public class HomeViewModel extends AndroidViewModel {

    private final MediatorLiveData<HomeViewState> homeViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<HomeViewState> getHomeViewStateMediatorLiveData() {
        return homeViewStateMediatorLiveData;
    }


    public HomeViewModel(@NonNull Application application,
                         UserRepository userRepository) {
        super(application);

        MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();

        userRepository.getCurrentUser().addOnSuccessListener(user -> {
            if (user != null) {
                currentUserLiveData.setValue(user);
            }
        });

        homeViewStateMediatorLiveData.addSource(currentUserLiveData, currentUser -> {
            combine(currentUser);
        });

    }


    private void combine(@Nullable User currentUser) {
        homeViewStateMediatorLiveData.setValue(
                new HomeViewState(
                        currentUser
                )
        );
    }

}
