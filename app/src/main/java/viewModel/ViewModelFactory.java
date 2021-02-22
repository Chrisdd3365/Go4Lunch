package viewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.MainApplication;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import model.repository.NearbyRestaurantsViewModel;
import com.google.android.gms.location.LocationServices;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sInstance;
    private final Application application;
    private final CurrentLocationRepository currentLocationRepository;

    public static ViewModelFactory getInstance() {
        if (sInstance == null) {
            sInstance = new ViewModelFactory();
        }
        return sInstance;
    }

    private ViewModelFactory() {
        this.application = MainApplication.getApplication();
        this.currentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(application));
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NearbyRestaurantsViewModel.class)) {
            //TODO: rename MapViewModel
            return (T) new NearbyRestaurantsViewModel(application, currentLocationRepository);
        } else if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            return (T) new RestaurantDetailsViewModel(application);
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
