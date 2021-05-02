package com.christophedurand.go4lunch.ui.mapView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.utils.MainApplication;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.google.android.gms.location.LocationServices;


public class MapViewModelFactory implements ViewModelProvider.Factory {

    private static MapViewModelFactory sInstance;
    private final Application application;
    private final MapViewRepository mapViewRepository;
    private final CurrentLocationRepository currentLocationRepository;


    public static MapViewModelFactory getInstance() {
        if (sInstance == null) {
            sInstance = new MapViewModelFactory();
        }
        return sInstance;
    }

    private MapViewModelFactory() {
        this.application = MainApplication.getApplication();
        this.mapViewRepository = MapViewRepository.getInstance();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(application, mapViewRepository, currentLocationRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
