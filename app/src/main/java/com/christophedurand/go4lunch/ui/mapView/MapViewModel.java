package com.christophedurand.go4lunch.ui.mapView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.API_KEY;

import kotlin.Pair;


public class MapViewModel extends AndroidViewModel {

    private final MediatorLiveData<MapViewState> mMapViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<MapViewState> getMapViewStateLiveData() {
        return mMapViewStateMediatorLiveData;
    }


    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final CurrentLocationRepository currentLocationRepository;
    private final PlaceNameRepository placeNameRepository;
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;


    public MapViewModel(@NonNull Application application,
                        NearbyRepository nearbyRepository,
                        @NonNull PermissionChecker permissionChecker,
                        @NonNull CurrentLocationRepository currentLocationRepository,
                        PlaceNameRepository placeNameRepository,
                        AutocompleteRepository autocompleteRepository,
                        UserRepository userRepository,
                        FirebaseAuth firebaseAuth) {

        super(application);

        this.permissionChecker = permissionChecker;
        this.currentLocationRepository = currentLocationRepository;
        this.placeNameRepository = placeNameRepository;
        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;


        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = autocompleteRepository.getAutocompleteMediatorLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        autocompleteMediatorLiveData,
                        value -> nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
                                value.getFirst(),
                                "restaurant",
                                value.getSecond().getLatitude() + "," + value.getSecond().getLongitude(),
                                "1000",
                                API_KEY)
                );

        LiveData<List<User>> usersListLiveData = userRepository.fetchAllUsers();


        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location, usersListLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(nearbyRestaurantsResponse, locationLiveData.getValue(), usersListLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(usersListLiveData, userList ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), locationLiveData.getValue(), userList));

    }

    private void combine(@Nullable NearbyRestaurantsResponse nearbyRestaurantsResponse,
                         @Nullable Location location,
                         @Nullable List<User> usersList) {

        if (location == null || nearbyRestaurantsResponse == null) {
            return;
        }

        List<MapMarker> mapMarkersList = new ArrayList<>();

        for (int i = 0; i < nearbyRestaurantsResponse.getResults().size(); i++) {
            String placeId = nearbyRestaurantsResponse.getResults().get(i).getPlaceId();
            String name = nearbyRestaurantsResponse.getResults().get(i).getName();
            String address = nearbyRestaurantsResponse.getResults().get(i).getVicinity();
            LatLng latLng = new LatLng(nearbyRestaurantsResponse.getResults().get(i).getGeometry().getLocation().getLat(), nearbyRestaurantsResponse.getResults().get(i).getGeometry().getLocation().getLng());

            String photoReference;
            if (nearbyRestaurantsResponse.getResults().get(i).getPhotos() != null) {
                photoReference = nearbyRestaurantsResponse.getResults().get(i).getPhotos().get(0).getPhotoReference();
            } else {
                photoReference = null;
            }

            mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference, usersList != null && getNumberOfWorkmates(placeId, usersList) != 0 ? "ic_restaurant_green_marker" : "ic_restaurant_red_marker"));
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        mapMarkersList
                )
        );
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (!permissionChecker.hasLocationPermission()) {
            currentLocationRepository.stopLocationRequest();
        } else {
            currentLocationRepository.initCurrentLocationUpdate();
        }
    }

    private int getNumberOfWorkmates(String placeId, List<User> userList) {
        int numberOfWorkmates = 0;

        for (User user : userList) {
            if (user.getRestaurant() != null && user.getRestaurant().getId().equals(placeId)) {
                numberOfWorkmates += 1;
            }
        }

        return numberOfWorkmates;
    }

    public void getQueriedRestaurant(String placeName) {
        placeNameRepository.getQueriedRestaurantByName(placeName);
    }

}
