package com.christophedurand.go4lunch.ui.workmatesView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.google.android.gms.location.LocationServices;


public class WorkmatesViewModelFactory implements ViewModelProvider.Factory {

    private static WorkmatesViewModelFactory sInstance;
    private final Application application;
    private final CurrentLocationRepository currentLocationRepository;
    private final NearbyRepository nearbyRepository;
    private final LifecycleOwner owner;


    public static WorkmatesViewModelFactory getInstance(LifecycleOwner owner) {
        if (sInstance == null) {
            sInstance = new WorkmatesViewModelFactory(owner);
        }
        return sInstance;
    }

    private WorkmatesViewModelFactory(LifecycleOwner owner) {
        this.application = MainApplication.getApplication();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
        this.nearbyRepository = NearbyRepository.getInstance();
        this.owner = owner;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(application, currentLocationRepository, nearbyRepository, owner);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
