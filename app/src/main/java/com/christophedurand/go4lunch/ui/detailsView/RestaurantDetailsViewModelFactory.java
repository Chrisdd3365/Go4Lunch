package com.christophedurand.go4lunch.ui.detailsView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.utils.MainApplication;


class RestaurantDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final RestaurantDetailsRepository restaurantDetailsRepository;
    private final String restaurantId;


    public RestaurantDetailsViewModelFactory(String restaurantId) {
        this.application = MainApplication.getApplication();
        this.restaurantDetailsRepository = RestaurantDetailsRepository.getInstance();
        this.restaurantId = restaurantId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            return (T) new RestaurantDetailsViewModel(application, restaurantDetailsRepository, restaurantId);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
