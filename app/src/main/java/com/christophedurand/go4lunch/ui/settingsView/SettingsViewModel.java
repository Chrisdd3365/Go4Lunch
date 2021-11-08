package com.christophedurand.go4lunch.ui.settingsView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;


public class SettingsViewModel extends AndroidViewModel {

    private final MediatorLiveData<SettingsViewState> settingsViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<SettingsViewState> getSettingsViewStateMediatorLiveData() {
        return settingsViewStateMediatorLiveData;
    }

    private final UserRepository _userRepository;


    public SettingsViewModel(@NonNull Application application,
                             UserRepository userRepository) {
        super(application);

        _userRepository = userRepository;

        MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();

        userRepository.getCurrentUser().addOnSuccessListener(user -> {
            if (user != null) {
                currentUserLiveData.setValue(user);
            }
        });

        settingsViewStateMediatorLiveData.addSource(currentUserLiveData, currentUser -> {
            combine(currentUser);
        });

    }


    private void combine(@Nullable User currentUser) {
        settingsViewStateMediatorLiveData.setValue(
                new SettingsViewState(
                        currentUser
                )
        );
    }

    public void updateHasSetNotifications(Boolean isChecked, String userId) {
        _userRepository.updateHasSetNotifications(isChecked, userId);
    }

}
