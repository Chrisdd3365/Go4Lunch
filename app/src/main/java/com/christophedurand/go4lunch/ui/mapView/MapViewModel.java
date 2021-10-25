package com.christophedurand.go4lunch.ui.mapView;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapViewModel extends AndroidViewModel {

    private final MediatorLiveData<MapViewState> mMapViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<MapViewState> getMapViewStateLiveData() {
        return mMapViewStateMediatorLiveData;
    }

    private final MutableLiveData<String> mPlaceNameMutableLiveData = new MutableLiveData<>();


    public MapViewModel(@NonNull Application application,
                        NearbyRepository nearbyRepository,
                        CurrentLocationRepository currentLocationRepository) {

        super(application);

        currentLocationRepository.initCurrentLocationUpdate();

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        AutocompleteLiveData autocompleteLiveData = new AutocompleteLiveData(mPlaceNameMutableLiveData, locationLiveData);

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        autocompleteLiveData,
                        value -> nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
                                value.first,
                                "restaurant",
                                value.second.getLatitude() + "," + value.second.getLongitude(),
                                "1000",
                                apiKey)
                );

        mMapViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), location, mPlaceNameMutableLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(nearbyRestaurantsResponse, locationLiveData.getValue(), mPlaceNameMutableLiveData.getValue()));

        mMapViewStateMediatorLiveData.addSource(mPlaceNameMutableLiveData, placeName ->
                combine(nearbyRestaurantsResponseLiveData.getValue(), locationLiveData.getValue(), placeName));

    }

    private void combine(@Nullable NearbyRestaurantsResponse nearbyRestaurantsResponse,
                         @Nullable Location location,
                         @Nullable String placeName) {

        if (location == null || nearbyRestaurantsResponse == null) {
            return;
        }

        List<MapMarker> mapMarkersList = new ArrayList<>();

        if (placeName != null) {
            for (Restaurant restaurant : nearbyRestaurantsResponse.getResults()) {
                if (restaurant.getName().equals(placeName)) {
                    String placeId = restaurant.getPlaceId();
                    String name = restaurant.getName();
                    String address = restaurant.getVicinity();
                    LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());

                    String photoReference;
                    if (restaurant.getPhotos() != null) {
                        photoReference = restaurant.getPhotos().get(0).getPhotoReference();
                    } else {
                        photoReference = null;
                    }

                    mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference, "ic_restaurant_red_marker"));
                }
            }
        } else {
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

                mapMarkersList.add(new MapMarker(placeId, name, address, latLng, photoReference, "ic_restaurant_red_marker"));
            }
        }

        mMapViewStateMediatorLiveData.setValue(
                new MapViewState(
                        location,
                        mapMarkersList
                )
        );
    }

    public void getQueriedRestaurant(String placeName) {
        mPlaceNameMutableLiveData.setValue(placeName);
    }

}
