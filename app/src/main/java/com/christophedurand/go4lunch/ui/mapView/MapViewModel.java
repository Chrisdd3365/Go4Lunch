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
    public LiveData<MapViewState> getMapViewStateLiveData() {
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

        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location));

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, response ->
                combine(response, locationLiveData.getValue()));

    }

    private void combine(@Nullable NearbyRestaurantsResponse response,
                         @Nullable Location location) {
        if (location == null || response == null) {
            return;
        }

        List<MapMarker> mapMarkersList = new ArrayList<>();
        for (int i=0; i<response.getResults().size(); i++) {
            String placeId = response.getResults().get(i).getPlaceId();
            String name = response.getResults().get(i).getName();
            String address = response.getResults().get(i).getVicinity();
            LatLng latLng = new LatLng(response.getResults().get(i).getGeometry().getLocation().getLat(), response.getResults().get(i).getGeometry().getLocation().getLng());

            String photoReference;
            if (response.getResults().get(i).getPhotos() != null) {
                photoReference = response.getResults().get(i).getPhotos().get(0).getPhotoReference();
            } else {
                photoReference = "photoReference";
            }

            mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference));
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        mapMarkersList
                )
        );
    }

}
