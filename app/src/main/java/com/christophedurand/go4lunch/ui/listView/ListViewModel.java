package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.ui.mapView.MapViewRepository;


import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class ListViewModel extends AndroidViewModel  {

    private final MediatorLiveData<ListViewState> listViewStateMediatorLiveData = new MediatorLiveData<>();


    public ListViewModel(@NonNull Application application,
                         CurrentLocationRepository currentLocationRepository,
                         MapViewRepository mapViewRepository) {

        super(application);

        currentLocationRepository.initCurrentLocationUpdate();
        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> mapViewRepository.getNearbyRestaurantsResponseLiveData(
                                "restaurant",
                                location.getLatitude() + "," + location.getLongitude(),
                                "1000",
                                apiKey)
                );

        listViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, nearbyRestaurantsResponseLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponse)
        );

    }


    public LiveData<ListViewState> getListViewStateMediatorLiveData() {
        return listViewStateMediatorLiveData;
    }


    private void combine(@Nullable Location location,
                         @Nullable NearbyRestaurantsResponse response) {

        if (location == null) {
            return;
        }

        listViewStateMediatorLiveData.setValue(
                new ListViewState(
                        location,
                        response == null ? null : response.results
                )
        );

    }

//    public static String getDistanceFromLastKnownUserLocation(Location currentLocation, Location restaurantLocation) {
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





