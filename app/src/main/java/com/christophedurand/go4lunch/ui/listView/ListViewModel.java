package com.christophedurand.go4lunch.ui.listView;

import android.app.Application;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

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
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

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


    private void combine(@Nullable Location currentLocation,
                         @Nullable NearbyRestaurantsResponse response,
                         @Nullable Map<String, RestaurantDetailsResponse> map) {

        if (currentLocation == null) {
            return;
        }

        List<RestaurantViewState> restaurantViewStatesList = new ArrayList<>();

        if (response != null) {
            for (Restaurant restaurant : response.getResults()) {
                RestaurantDetailsResponse restaurantDetailsResponse = map.get(restaurant.getPlaceId());
                if (restaurantDetailsResponse != null) {
                    RestaurantViewState restaurantViewState = new RestaurantViewState(
                            restaurantDetailsResponse.getResult().getFormattedAddress(),
                            getDistanceFromLastKnownUserLocation(currentLocation, restaurant.getGeometry().getLocation()),
                            restaurantDetailsResponse.getResult().getName(),
                            restaurantDetailsResponse.getResult().getPlaceId(),
                            getConvertedRatingWith(restaurantDetailsResponse.getResult().getRating()),
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
                    RestaurantViewState restaurantViewState = new RestaurantViewState(
                            restaurant.getVicinity(),
                            getDistanceFromLastKnownUserLocation(currentLocation, restaurant.getGeometry().getLocation()),
                            restaurant.getName(),
                            restaurant.getPlaceId(),
                            getConvertedRatingWith(restaurant.getRating()),
                            getPhotoUrl(restaurant.getPhotos()),
                            getOpeningHours(restaurant.getOpeningHours())
                    );
                    restaurantViewStatesList.add(restaurantViewState);
                }
            }
        }

        listViewStateMediatorLiveData.setValue(
                new ListViewState(
                        currentLocation,
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
            return openingHours.getWeekdayText().get(LocalDate.now().getDayOfWeek().getValue() - 1);

        } else if (openingHours != null && openingHours.isOpenNow() != null) {

            if (openingHours.isOpenNow()) {
                return "Open";
            } else {
                return "Closed";
            }

        }

        return "Opening Hours Unavailable";
    }

    private double getConvertedRatingWith(double rating) {
        double convertedRating;

        if (rating > 1.00 && rating <= 1.66) {
            convertedRating = 1;
            return convertedRating;
        } else if (rating > 1.66 && rating <= 3.33) {
            convertedRating = 2;
            return convertedRating;
        } else if (rating > 3.33 && rating <= 5) {
            convertedRating = 3;
            return convertedRating;
        } else {
            convertedRating = 0;
            return convertedRating;
        }
    }

    private String getDistanceFromLastKnownUserLocation(@NonNull Location currentLocation, RestaurantLocation restaurantLocation) {

        Location location = new Location("restaurant location");
        location.setLatitude(restaurantLocation.getLat());
        location.setLongitude(restaurantLocation.getLng());

        float distance = currentLocation.distanceTo(location);
        return (int) distance + "m";
    }

}
