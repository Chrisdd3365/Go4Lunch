package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsRepository;
import com.christophedurand.go4lunch.ui.mapView.MapViewRepository;


import java.util.HashMap;
import java.util.Map;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class ListViewModel extends AndroidViewModel  {

    private final MediatorLiveData<ListViewState> listViewStateMediatorLiveData = new MediatorLiveData<>();


    public ListViewModel(@NonNull Application application,
                         CurrentLocationRepository currentLocationRepository,
                         MapViewRepository mapViewRepository,
                         RestaurantDetailsRepository restaurantDetailsRepository) {

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

        LiveData<Map<String, RestaurantDetailsResponse>> mapStringRestaurantDetailsLiveData =
                Transformations.map(
                        nearbyRestaurantsResponseLiveData,
                        response -> {
                            Map<String, RestaurantDetailsResponse> hashMap = new HashMap<>();
                            for (int i = 0; i<response.results.size(); i++) {
                                String placeId = response.results.get(i).placeId;
                                RestaurantDetailsResponse restaurantDetailsResponse = restaurantDetailsRepository.getRestaurantDetailsMutableLiveData(placeId).getValue();
                                hashMap.put(
                                        placeId,
                                        restaurantDetailsResponse
                                );
                            }
                            return hashMap;
                        }
                );


        listViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, nearbyRestaurantsResponseLiveData.getValue(), mapStringRestaurantDetailsLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponse, mapStringRestaurantDetailsLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(mapStringRestaurantDetailsLiveData, map ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponseLiveData.getValue(), map)
        );

    }


    public LiveData<ListViewState> getListViewStateMediatorLiveData() {
        return listViewStateMediatorLiveData;
    }


    private void combine(@Nullable Location location,
                         @Nullable NearbyRestaurantsResponse response,
                         @Nullable Map<String, RestaurantDetailsResponse> map) {

        if (location == null) {
            return;
        }

        listViewStateMediatorLiveData.setValue(
                new ListViewState(
                        location,
                        response == null ? null : response.results,
                        map
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





