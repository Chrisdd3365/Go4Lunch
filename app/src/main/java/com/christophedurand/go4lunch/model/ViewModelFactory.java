package com.christophedurand.go4lunch.model;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.utils.MainApplication;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.ui.listView.ListViewModel;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.google.android.gms.location.LocationServices;

import com.christophedurand.go4lunch.ui.mapView.MapViewRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sInstance;
    private final Application application;
    private final MapViewRepository mapViewRepository;
    private final CurrentLocationRepository currentLocationRepository;

    public static ViewModelFactory getInstance() {
        if (sInstance == null) {
            sInstance = new ViewModelFactory();
        }
        return sInstance;
    }

    private ViewModelFactory() {
        this.application = MainApplication.getApplication();
        this.mapViewRepository = MapViewRepository.getInstance();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(application, mapViewRepository, currentLocationRepository);
        } else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(application);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
