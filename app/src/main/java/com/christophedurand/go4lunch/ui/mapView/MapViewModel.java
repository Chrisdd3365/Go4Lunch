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
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapViewModel extends AndroidViewModel {

    private final MediatorLiveData<MapViewState> mMapViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<MapViewState> getViewStateLiveData() {
        return mMapViewStateMediatorLiveData;
    }


    public MapViewModel(@NonNull Application application,
                        NearbyRepository nearbyRepository,
                        CurrentLocationRepository currentLocationRepository) {
        super(application);

        currentLocationRepository.initCurrentLocationUpdate();

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
                                "restaurant",
                                location.getLatitude() + "," + location.getLongitude(),
                                "1000",
                                apiKey)
                );

        LiveData<List<MapMarker>> mapMarkersListLiveData =
                Transformations.map(
                        nearbyRestaurantsResponseLiveData,
                        response -> {
                            List<MapMarker> mapMarkersList = new ArrayList<>();
                            for (int i=0; i<response.results.size(); i++) {
                                String placeId = response.results.get(i).placeId;
                                String name = response.results.get(i).name;
                                String address = response.results.get(i).vicinity;
                                LatLng latLng = new LatLng(response.results.get(i).geometry.location.lat, response.results.get(i).geometry.location.lng);
                                String photoReference = response.results.get(i).getPhotos().get(0).getPhotoReference();

                                mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference));
                            }
                            return mapMarkersList;
                        }
                );

        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location, mapMarkersListLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, response ->
                combine(response, locationLiveData.getValue(), mapMarkersListLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(mapMarkersListLiveData, markersList ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), locationLiveData.getValue(), markersList));
    }

    private void combine(@Nullable NearbyRestaurantsResponse response,
                         @Nullable Location location,
                         List<MapMarker> mapMarkersList) {
        if (location == null) {
            return;
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        response == null ? null : response.results,
                        mapMarkersList
                )
        );
    }

}
