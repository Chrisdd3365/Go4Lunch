package com.christophedurand.go4lunch.ui.workmatesView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.utils.MainApplication;


public class WorkmatesViewModelFactory implements ViewModelProvider.Factory {

    private static WorkmatesViewModelFactory sInstance;
    private final Application application;
    private final LifecycleOwner owner;


    public static WorkmatesViewModelFactory getInstance(LifecycleOwner owner) {
        if (sInstance == null) {
            sInstance = new WorkmatesViewModelFactory(owner);
        }
        return sInstance;
    }

    private WorkmatesViewModelFactory(LifecycleOwner owner) {
        this.application = MainApplication.getApplication();
        this.owner = owner;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(application, owner);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}