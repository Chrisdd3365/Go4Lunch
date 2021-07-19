package com.christophedurand.go4lunch.ui.mapView;

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
import com.christophedurand.go4lunch.model.pojo.AutocompleteResponse;
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
                        AutocompleteRepository autocompleteRepository,
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

        LiveData<AutocompleteResponse> autocompleteResponseLiveData =
                autocompleteRepository.getAutocompleteResponseLiveData("input", apiKey);

        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(autocompleteResponseLiveData.getValue(), nearbyRestaurantsResponseLiveData.getValue(), location));

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(autocompleteResponseLiveData.getValue(), nearbyRestaurantsResponse, locationLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(autocompleteResponseLiveData, autocompleteResponse ->
                combine(autocompleteResponse, nearbyRestaurantsResponseLiveData.getValue(), locationLiveData.getValue()));

    }

    private void combine(@Nullable AutocompleteResponse autocompleteResponse,
                         @Nullable NearbyRestaurantsResponse nearbyRestaurantsResponse,
                         @Nullable Location location) {

        if (location == null || nearbyRestaurantsResponse == null || autocompleteResponse == null) {
            return;
        }

        List<MapMarker> mapMarkersList = new ArrayList<>();
        for (int i=0; i<nearbyRestaurantsResponse.getResults().size(); i++) {
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

            mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference));
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        mapMarkersList,
                        new AutocompleteRestaurantViewState()
                )
        );
    }

}
