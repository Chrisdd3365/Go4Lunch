package com.christophedurand.go4lunch.ui;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.ui.listView.ListViewModel;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.ui.workmatesView.WorkmatesViewModel;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.google.android.gms.location.LocationServices;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sInstance;

    private final Application mApplication;

    private final NearbyRepository mNearbyRepository;
    private final DetailsRepository mDetailsRepository;
    @NonNull
    private final CurrentLocationRepository mCurrentLocationRepository;
    @NonNull
    private final PermissionChecker mPermissionChecker;


    public static ViewModelFactory getInstance() {
        if (sInstance == null) {
            synchronized (ViewModelFactory.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new ViewModelFactory(
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

    private ViewModelFactory(@NonNull PermissionChecker permissionChecker,
                             @NonNull CurrentLocationRepository currentLocationRepository) {
        this.mApplication = MainApplication.getApplication();
        this.mNearbyRepository = NearbyRepository.getInstance();
        this.mDetailsRepository = DetailsRepository.getInstance();
        this.mCurrentLocationRepository = currentLocationRepository;
        this.mPermissionChecker = permissionChecker;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        LiveData<Location> locationLiveData = mCurrentLocationRepository.getCurrentLocationLiveData();
        PlaceNameRepository placeNameRepository = new PlaceNameRepository();
        LiveData<String> placeNameMutableLiveData = placeNameRepository.getPlaceNameMutableLiveData();
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
             return (T) new MapViewModel(
                     mApplication,
                     mNearbyRepository,
                     mPermissionChecker,
                     mCurrentLocationRepository,
                     placeNameRepository,
                     new AutocompleteRepository(placeNameMutableLiveData, locationLiveData)
             );
         } else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(
                    mApplication,
                    mPermissionChecker,
                    mCurrentLocationRepository,
                    placeNameRepository,
                    mNearbyRepository,
                    mDetailsRepository,
                    new AutocompleteRepository(placeNameMutableLiveData, locationLiveData)
            );
        } else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(
                    mApplication,
                    mPermissionChecker,
                    mCurrentLocationRepository,
                    mNearbyRepository
            );
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
