package com.christophedurand.go4lunch.ui.mapView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.google.android.gms.location.LocationServices;


public class MapViewModelFactory implements ViewModelProvider.Factory {

    private static MapViewModelFactory sInstance;
    private final Application application;
    private final NearbyRepository nearbyRepository;
    @NonNull
    private final CurrentLocationRepository currentLocationRepository;
    @NonNull
    private final PermissionChecker permissionChecker;


    public static MapViewModelFactory getInstance() {
        if (sInstance == null) {
            synchronized (MapViewModelFactory.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new MapViewModelFactory(
                            new PermissionChecker(
                                    application
                            ),
                            new CurrentLocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            )
                    );
                }
            }
        }
        return sInstance;
    }

    private MapViewModelFactory(@NonNull PermissionChecker permissionChecker,
                                @NonNull CurrentLocationRepository currentLocationRepository) {
        this.application = MainApplication.getApplication();
        this.nearbyRepository = NearbyRepository.getInstance();
        this.permissionChecker = permissionChecker;
        this.currentLocationRepository = currentLocationRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(application, nearbyRepository, permissionChecker, currentLocationRepository, new PlaceNameRepository());
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
