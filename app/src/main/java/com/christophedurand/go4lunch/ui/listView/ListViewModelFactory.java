package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsRepository;
import com.christophedurand.go4lunch.ui.mapView.MapViewRepository;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.google.android.gms.location.LocationServices;


class ListViewModelFactory implements ViewModelProvider.Factory {

    private static ListViewModelFactory sInstance;
    private final Application application;
    private final CurrentLocationRepository currentLocationRepository;
    private final MapViewRepository mapViewRepository;
    private final RestaurantDetailsRepository restaurantDetailsRepository;


    public static ListViewModelFactory getInstance() {
        if (sInstance == null) {
            sInstance = new ListViewModelFactory();
        }
        return sInstance;
    }

    private ListViewModelFactory() {
        this.application = MainApplication.getApplication();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
        this.mapViewRepository = MapViewRepository.getInstance();
        this.restaurantDetailsRepository = RestaurantDetailsRepository.getInstance();
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(
                    application,
                    currentLocationRepository,
                    mapViewRepository,
                    restaurantDetailsRepository
            );
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
