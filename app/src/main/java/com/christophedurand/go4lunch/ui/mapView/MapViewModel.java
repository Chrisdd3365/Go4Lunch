package com.christophedurand.go4lunch.ui.mapView;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;

import model.pojo.NearbyRestaurantsResponse;
import model.repository.NearbyRestaurantsRepository;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapViewModel extends AndroidViewModel {

    private final MediatorLiveData<MapViewState> mNearbyRestaurantsListMediatorLiveData = new MediatorLiveData<>();

    public LiveData<MapViewState> getViewStateLiveData() {
        return mNearbyRestaurantsListMediatorLiveData;
    }

    public MapViewModel(@NonNull Application application,
                        NearbyRestaurantsRepository nearbyRestaurantsRepository,
                        CurrentLocationRepository currentLocationRepository) {
        super(application);

        currentLocationRepository.initCurrentLocationUpdate();

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> nearbyRestaurantsRepository.getListOfNearbyRestaurants(
                        "restaurant",
                        location.getLatitude() + "," + location.getLongitude(),
                        "1000",
                        apiKey));

        mNearbyRestaurantsListMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, response ->
                combine(response, locationLiveData.getValue()));

        mNearbyRestaurantsListMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location));

    }

    private void combine(@Nullable NearbyRestaurantsResponse response, @Nullable Location location) {
        if (location == null) {
            return;
        }

        mNearbyRestaurantsListMediatorLiveData.setValue(new MapViewState(location, response == null ? null : response.results));

    }


//    public String getDistanceFromLastKnownUserLocation(int position, Location location) {
//        Restaurant restaurant = Objects.requireNonNull(
//                loadNearbyRestaurantsData(location)
//                        .getValue()
//        ).results.get(position);
//
//        Location restaurantLocation = new Location("restaurant location");
//        restaurantLocation.setLatitude(restaurant.geometry.location.lat);
//        restaurantLocation.setLongitude(restaurant.geometry.location.lng);
//
//        Location currentLocation = locationLiveData.getValue();
//        assert currentLocation != null;
//        float distance = currentLocation.distanceTo(restaurantLocation);
//        Log.d("DISTANCE", "distance is : " + distance);
//
//        return (int) distance + "m";
//    }

}
