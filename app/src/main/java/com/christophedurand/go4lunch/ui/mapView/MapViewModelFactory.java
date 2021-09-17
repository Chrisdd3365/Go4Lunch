package com.christophedurand.go4lunch.ui.mapView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.google.android.gms.location.LocationServices;


public class MapViewModelFactory implements ViewModelProvider.Factory {

    private static MapViewModelFactory sInstance;
    private final Application application;
    private final AutocompleteRepository mAutocompleteRepository;
    private final NearbyRepository mNearbyRepository;
    private final CurrentLocationRepository currentLocationRepository;
    private final String input;


    public static MapViewModelFactory getInstance(String input) {
        if (sInstance == null) {
            sInstance = new MapViewModelFactory(input);
        }
        return sInstance;
    }

    private MapViewModelFactory(String input) {
        this.application = MainApplication.getApplication();
        this.mAutocompleteRepository = AutocompleteRepository.getInstance();
        this.mNearbyRepository = NearbyRepository.getInstance();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
        this.input = input;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(application, mAutocompleteRepository, mNearbyRepository, currentLocationRepository, input);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
