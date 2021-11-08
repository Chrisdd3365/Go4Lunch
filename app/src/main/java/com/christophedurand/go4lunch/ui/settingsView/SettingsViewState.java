package com.christophedurand.go4lunch.ui.settingsView;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.User;


public class SettingsViewState {

    @Nullable
    private final User currentUser;


    public SettingsViewState(@Nullable User currentUser) {
        this.currentUser = currentUser;
    }


    @Nullable
    public User getCurrentUser() {
        return currentUser;
    }

}