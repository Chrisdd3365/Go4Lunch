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
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsViewModel;
import com.christophedurand.go4lunch.ui.homeView.HomeViewModel;
import com.christophedurand.go4lunch.ui.listView.ListViewModel;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.ui.settingsView.SettingsViewModel;
import com.christophedurand.go4lunch.ui.workmatesView.WorkmatesViewModel;
import com.christophedurand.go4lunch.utils.MainApplication;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    @NonNull
    private final PermissionChecker mPermissionChecker;
    private final FirebaseAuth mFirebaseAuth;

    private final UserRepository mUserRepository;
    private final NearbyRepository mNearbyRepository;
    private final DetailsRepository mDetailsRepository;
    @NonNull
    private final CurrentLocationRepository mCurrentLocationRepository;

    private final String restaurantId;


    public ViewModelFactory(String restaurantId) {
        this.restaurantId = restaurantId;
        this.mApplication = MainApplication.getApplication();
        this.mPermissionChecker = new PermissionChecker(mApplication);
        this.mFirebaseAuth = FirebaseAuth.getInstance();
        this.mNearbyRepository = NearbyRepository.getInstance();
        this.mDetailsRepository = DetailsRepository.getInstance();
        this.mCurrentLocationRepository = new CurrentLocationRepository(LocationServices.getFusedLocationProviderClient(mApplication));
        this.mUserRepository = UserRepository.getInstance();
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
                     new AutocompleteRepository(placeNameMutableLiveData, locationLiveData),
                     mUserRepository,
                     mFirebaseAuth
             );
         } else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(
                    mApplication,
                    mPermissionChecker,
                    mCurrentLocationRepository,
                    placeNameRepository,
                    mNearbyRepository,
                    mDetailsRepository,
                    new AutocompleteRepository(placeNameMutableLiveData, locationLiveData),
                    mUserRepository,
                    mFirebaseAuth
            );
        } else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(
                    mApplication,
                    mUserRepository,
                    mFirebaseAuth
            );
        } else if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            return (T) new RestaurantDetailsViewModel(
                    mApplication,
                    mDetailsRepository,
                    mUserRepository,
                    mFirebaseAuth,
                    restaurantId
            );
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(
                    mApplication,
                    mUserRepository
            );
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(
                    mApplication,
                    mUserRepository
            );
        }

        throw new IllegalArgumentException("Unknown ViewModel class $modelClass");
    }

}
