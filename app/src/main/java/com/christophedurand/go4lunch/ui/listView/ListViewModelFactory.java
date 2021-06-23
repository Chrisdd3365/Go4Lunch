package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.google.android.gms.location.LocationServices;


class ListViewModelFactory implements ViewModelProvider.Factory {

    private static ListViewModelFactory sInstance;
    private final Application application;
    private final CurrentLocationRepository currentLocationRepository;
    private final NearbyRepository mNearbyRepository;
    private final DetailsRepository mDetailsRepository;


    public static ListViewModelFactory getInstance() {
        if (sInstance == null) {
            sInstance = new ListViewModelFactory();
        }
        return sInstance;
    }

    private ListViewModelFactory() {
        this.application = MainApplication.getApplication();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
        this.mNearbyRepository = NearbyRepository.getInstance();
        this.mDetailsRepository = DetailsRepository.getInstance();
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(
                    application,
                    currentLocationRepository,
                    mNearbyRepository,
                    mDetailsRepository
            );
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
