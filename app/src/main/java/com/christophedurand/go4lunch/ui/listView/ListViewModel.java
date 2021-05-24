

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
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class ListViewModel extends AndroidViewModel  {

    private final List<String> alreadyQueriedPlaceIds = new ArrayList<>();

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

        for (Restaurant restaurant : response.getResults()) {
            RestaurantDetailsResponse restaurantDetailsResponse = map.get(restaurant.getPlaceId());
            if (restaurantDetailsResponse != null) {
                //TODO: get distance
                RestaurantViewState restaurantViewState = new RestaurantViewState(
                        restaurantDetailsResponse.getResult().getFormattedAddress(),
                        null,
                        restaurantDetailsResponse.getResult().getName(),
                        restaurantDetailsResponse.getResult().getPlaceId(),
                        restaurantDetailsResponse.getResult().getRating(),
                        getPhotoUrl(restaurantDetailsResponse.getResult().getPhotos()),
                        getOpeningHours(restaurantDetailsResponse.getResult().getOpeningHours())
                );
                restaurantViewStatesList.add(restaurantViewState);
            } else {
                if (!alreadyQueriedPlaceIds.contains(restaurant.getPlaceId())) {
                    alreadyQueriedPlaceIds.add(restaurant.getPlaceId());
                    placeIdRestaurantDetailsMapLiveData.addSource(
                            detailsRepository.getRestaurantDetailsMutableLiveData(restaurant.getPlaceId()),
                            detailsResponse -> {
                                Map<String, RestaurantDetailsResponse> mapInstance = placeIdRestaurantDetailsMapLiveData.getValue();
                                mapInstance.put(restaurant.getPlaceId(), detailsResponse);
                                placeIdRestaurantDetailsMapLiveData.setValue(mapInstance);
                            }
                    );
                }
                //TODO: get distance
                RestaurantViewState restaurantViewState = new RestaurantViewState(
                        restaurant.getVicinity(),
                        null,
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getRating(),
                        getPhotoUrl(restaurant.getPhotos()),
                        getOpeningHours(restaurant.getOpeningHours())
                );
                restaurantViewStatesList.add(restaurantViewState);
            }
        }

        listViewStateMediatorLiveData.setValue(
                new ListViewState(
                        location,
                        restaurantViewStatesList)
        );
    }

    private String getPhotoUrl(@Nullable List<Photo> photoList) {
        if (photoList != null) {
            for (Photo photo : photoList) {
                if (photo.getPhotoReference() != null && !photo.getPhotoReference().isEmpty()) {
                    return photo.getPhotoReference();
                }
            }
        }
        return null;
    }

    //TODO: build string
    private String getOpeningHours(@Nullable OpeningHours openingHours) {
        if (openingHours != null && openingHours.getPeriods() != null) {
            return "toto";
        } else if (openingHours != null && openingHours.isOpenNow() != null) {
            if (openingHours.isOpenNow()) {
                return "Open";
            } else {
                return "Closed";
            }
        }
        return "Unkwnown";
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
