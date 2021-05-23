

package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class ListViewModel extends AndroidViewModel  {

    private final DetailsRepository detailsRepository;

    // "Final product"
    private final MediatorLiveData<ListViewState> listViewStateMediatorLiveData = new MediatorLiveData<>();

    // DetailResponse aggregator
    private final MediatorLiveData<Map<String, RestaurantDetailsResponse>> placeIdRestaurantDetailsMapLiveData = new MediatorLiveData<>();


    public ListViewModel(@NonNull Application application,
                         CurrentLocationRepository currentLocationRepository,
                         NearbyRepository nearbyRepository,
                         DetailsRepository detailsRepository) {
        super(application);

        this.detailsRepository = detailsRepository;

        placeIdRestaurantDetailsMapLiveData.setValue(new HashMap<>());

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

        listViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, nearbyRestaurantsResponseLiveData.getValue(), placeIdRestaurantDetailsMapLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponse, placeIdRestaurantDetailsMapLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(placeIdRestaurantDetailsMapLiveData, map ->
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

        List<RestaurantViewState> restaurantViewStatesList = new ArrayList<>();
        final OpeningHoursViewState openingHoursViewState;

        for (Restaurant restaurant : response.results) {
            RestaurantDetailsResponse restaurantDetailsResponse = map.get(restaurant.getPlaceId());
            if (restaurantDetailsResponse != null)  {
                map.put(restaurant.getPlaceId(), restaurantDetailsResponse);
                placeIdRestaurantDetailsMapLiveData.setValue(map);
                // TODO Do map, but without details (opening hours will be "closed" or "open" only
                Transformations.map(
                        placeIdRestaurantDetailsMapLiveData,
                        placeIdRestaurantDetailsMap -> {
                            openingHoursViewState = new OpeningHoursViewState(
                                    placeIdRestaurantDetailsMap.get(restaurant.placeId).result.getOpeningHours().isOpenNow(),
                                    placeIdRestaurantDetailsMap.get(restaurant.placeId).result.openingHours.periods);
                            return openingHoursViewState;
                        }
                );
            } else {
                placeIdRestaurantDetailsMapLiveData.addSource(
                        detailsRepository.getRestaurantDetailsMutableLiveData(restaurant.getPlaceId()),
                        detailsResponse -> {
                            Map<String, RestaurantDetailsResponse> mapInstance = placeIdRestaurantDetailsMapLiveData.getValue();
                            mapInstance.put(restaurant.getPlaceId(), detailsResponse);
                            placeIdRestaurantDetailsMapLiveData.setValue(mapInstance);
                        }
                );
                // TODO Do map, but without details (opening hours will be "closed" or "open" only
                Transformations.map(
                        placeIdRestaurantDetailsMapLiveData,
                        placeIdRestaurantDetailsMap -> {
                            openingHoursViewState = new OpeningHoursViewState(
                                    placeIdRestaurantDetailsMap.get(restaurant.placeId).result.getOpeningHours().isOpenNow(),
                                    placeIdRestaurantDetailsMap.get(restaurant.placeId).result.openingHours.periods);
                            return openingHoursViewState;
                        }
                );
            }
            // TODO Mapper les Restaurants "Techniques" en ViewState (pour calculer openings hours par exemple)
            Transformations.map(
                    placeIdRestaurantDetailsMapLiveData,
                    placeIdRestaurantDetailsMap -> {
                        RestaurantViewState restaurantViewState =
                                new RestaurantViewState(
                                        restaurant.vicinity,
                                        restaurant.geometry.location,
                                        restaurant.icon,
                                        restaurant.name,
                                        restaurant.placeId,
                                        restaurant.rating,
                                        restaurant.getPhotos(),
                                        openingHoursViewState
                                );
                        restaurantViewStatesList.add(restaurantViewState);
                        return restaurantViewStatesList;
                    }
            );
        }

        listViewStateMediatorLiveData.setValue(
                new ListViewState(
                        location,
                        restaurantViewStatesList)
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
