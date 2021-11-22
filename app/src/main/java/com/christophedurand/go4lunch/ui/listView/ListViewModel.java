package com.christophedurand.go4lunch.ui.listView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.API_KEY;

import kotlin.Pair;


public class ListViewModel extends AndroidViewModel  {

    // "Final product"
    private final MediatorLiveData<ListViewState> listViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<ListViewState> getListViewStateMediatorLiveData() {
        return listViewStateMediatorLiveData;
    }

    // DetailResponse aggregator
    private final MediatorLiveData<Map<String, RestaurantDetailsResponse>> placeIdRestaurantDetailsMapMediatorLiveData = new MediatorLiveData<>();

    private final List<String> alreadyQueriedPlaceIds = new ArrayList<>();


    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final CurrentLocationRepository currentLocationRepository;
    private final DetailsRepository detailsRepository;
    private final PlaceNameRepository placeNameRepository;
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;


    public ListViewModel(@NonNull Application application,
                         @NonNull PermissionChecker permissionChecker,
                         @NonNull CurrentLocationRepository currentLocationRepository,
                         PlaceNameRepository placeNameRepository,
                         NearbyRepository nearbyRepository,
                         DetailsRepository detailsRepository,
                         AutocompleteRepository autocompleteRepository,
                         UserRepository userRepository,
                         FirebaseAuth firebaseAuth) {

        super(application);

        this.permissionChecker = permissionChecker;
        this.currentLocationRepository = currentLocationRepository;
        this.placeNameRepository = placeNameRepository;
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;


        placeIdRestaurantDetailsMapMediatorLiveData.setValue(new HashMap<>());

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = autocompleteRepository.getAutocompleteMediatorLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        autocompleteMediatorLiveData,
                        value ->
                            nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
                                    value.getFirst(),
                                    "restaurant",
                                    value.getSecond().getLatitude() + "," + value.getSecond().getLongitude(),
                                    "1000",
                                    API_KEY)
                );

        LiveData<List<User>> usersListLiveData = userRepository.fetchAllUsers();


        listViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, nearbyRestaurantsResponseLiveData.getValue(), placeIdRestaurantDetailsMapMediatorLiveData.getValue(), usersListLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponse, placeIdRestaurantDetailsMapMediatorLiveData.getValue(), usersListLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(placeIdRestaurantDetailsMapMediatorLiveData, map ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponseLiveData.getValue(), map, usersListLiveData.getValue())
        );

        listViewStateMediatorLiveData.addSource(usersListLiveData, usersList ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponseLiveData.getValue(), placeIdRestaurantDetailsMapMediatorLiveData.getValue(), usersList)
        );

    }


    private void combine(@Nullable Location currentLocation,
                         @Nullable NearbyRestaurantsResponse response,
                         @Nullable Map<String, RestaurantDetailsResponse> map,
                         @Nullable List<User> usersList) {

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
                            getOpeningHours(restaurantDetailsResponse.getResult().getOpeningHours()),
                            buildNumberOfWorkmatesString(usersList != null ? getNumberOfWorkmates(restaurant.getPlaceId(), usersList) : 0)                    );

                    restaurantViewStatesList.add(restaurantViewState);
                } else {
                    if (!alreadyQueriedPlaceIds.contains(restaurant.getPlaceId())) {
                        alreadyQueriedPlaceIds.add(restaurant.getPlaceId());
                        placeIdRestaurantDetailsMapMediatorLiveData.addSource(
                                detailsRepository.getRestaurantDetailsMutableLiveData(restaurant.getPlaceId()),
                                detailsResponse -> {
                                    Map<String, RestaurantDetailsResponse> mapInstance = placeIdRestaurantDetailsMapMediatorLiveData.getValue();
                                    mapInstance.put(restaurant.getPlaceId(), detailsResponse);
                                    placeIdRestaurantDetailsMapMediatorLiveData.setValue(mapInstance);
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
                            getOpeningHours(restaurant.getOpeningHours()),
                            buildNumberOfWorkmatesString(usersList != null ? getNumberOfWorkmates(restaurant.getPlaceId(), usersList) : 0)
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

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (!permissionChecker.hasLocationPermission()) {
            currentLocationRepository.stopLocationRequest();
        } else {
            currentLocationRepository.initCurrentLocationUpdate();
        }
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
                return getApplication().getString(R.string.open_title);
            } else {
                return getApplication().getString(R.string.closed_title);
            }

        }

        return getApplication().getString(R.string.opening_hours_unavailable);
    }

    private double getConvertedRatingWith(double rating) {

        long convertedRating = Math.round(rating * 3 / 5);

        if (convertedRating <= 0) {
            return 0;
        } else if (convertedRating < 1.5) {
            return 1;
        } else if (convertedRating >= 1.5 && convertedRating <= 2.5) {
            return 2;
        } else {
            return 3;
        }

    }

    private String getDistanceFromLastKnownUserLocation(@NonNull Location currentLocation, RestaurantLocation restaurantLocation) {

        Location location = new Location("restaurant location");
        location.setLatitude(restaurantLocation.getLat());
        location.setLongitude(restaurantLocation.getLng());

        float distance = currentLocation.distanceTo(location);
        return (int) distance + "m";
    }

    private String buildNumberOfWorkmatesString(int numberOfWorkmates) {
        return "(" + numberOfWorkmates + ")";
    }

    private int getNumberOfWorkmates(String placeId, List<User> userList) {
        int numberOfWorkmates = 0;

        for (User user : userList) {
           if (user.getRestaurant() != null && user.getRestaurant().getId().equals(placeId)) {
               numberOfWorkmates += 1;
            }
        }

        return numberOfWorkmates;
    }

    public void getRestaurantQuery(String placeName) {
        placeNameRepository.getQueriedRestaurantByName(placeName);
    }

}
