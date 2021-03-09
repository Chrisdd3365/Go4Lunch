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
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapViewModel extends AndroidViewModel {

    private final MediatorLiveData<MapViewState> mMapViewStateMediatorLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<MapViewState.MapMarker> mMapMarkerMediatorLiveData = new MediatorLiveData<>();

    public LiveData<MapViewState> getViewStateLiveData() {
        return mMapViewStateMediatorLiveData;
    }


    public MapViewModel(@NonNull Application application,
                        MapViewRepository mapViewRepository,
                        CurrentLocationRepository currentLocationRepository) {
        super(application);

        currentLocationRepository.initCurrentLocationUpdate();

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> mapViewRepository.getListOfNearbyRestaurants(
                        "restaurant",
                        location.getLatitude() + "," + location.getLongitude(),
                        "1000",
                        apiKey));

        LiveData<List<MapViewState.MapMarker>> mapMarkersListLiveData =
                Transformations.map(
                        nearbyRestaurantsResponseLiveData,
                        response -> {
                            List<MapViewState.MapMarker> mapMarkersList = new ArrayList<>();
                            for (int i=0; i<response.results.size(); i++) {
                                String placeId = response.results.get(i).placeId;
                                String name = response.results.get(i).name;
                                String address = response.results.get(i).formattedAddress;
                                mapMarkersList.add(new MapViewState.MapMarker(placeId, name, address));
                            }
                            return mapMarkersList;
                        });

        mMapMarkerMediatorLiveData.addSource(mapMarkersListLiveData, list -> {
            for(int i=0; i<list.size(); i++) {
                combine(list.get(i));
            }
        });

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, response ->
                combine(response, locationLiveData.getValue(), mMapMarkerMediatorLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location, mMapMarkerMediatorLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(mMapMarkerMediatorLiveData, marker ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), locationLiveData.getValue(), marker));

    }

    private void combine(@NonNull MapViewState.MapMarker mapMarker) {
        mMapMarkerMediatorLiveData.setValue(mapMarker);
    }

    private void combine(@Nullable NearbyRestaurantsResponse response,
                         @Nullable Location location,
                         @NonNull MapViewState.MapMarker mapMarker) {
        if (location == null) {
            return;
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        response == null ? null : response.results,
                        mapMarker
                )
        );
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
