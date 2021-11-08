package com.christophedurand.go4lunch;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.pojo.Geometry;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.ui.mapView.MapViewState;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.ui.workmatesView.Restaurant;
import com.christophedurand.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.apiKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import kotlin.Pair;


@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);

    private final CurrentLocationRepository currentLocationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final NearbyRepository nearbyRepository = Mockito.mock(NearbyRepository.class);
    private final PlaceNameRepository placeNameRepository = Mockito.mock(PlaceNameRepository.class);
    private final AutocompleteRepository autocompleteRepository = Mockito.mock(AutocompleteRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<String> placeNameMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private final MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersListLiveData = new MutableLiveData<>();

    private MapViewModel mapViewModel;


    @Before
    public void setUp() {

        Location userCurrentLocation = Mockito.mock(Location.class);
        Mockito.doReturn(30.0).when(userCurrentLocation).getLatitude();
        Mockito.doReturn(42.0).when(userCurrentLocation).getLongitude();
        currentLocationLiveData.setValue(userCurrentLocation);
        Mockito.doReturn(currentLocationLiveData)
                .when(currentLocationRepository)
                .getCurrentLocationLiveData();

        String placeName = "name1";
        placeNameMutableLiveData.setValue(placeName);

        Pair<String, Location> autocompleteResponse = new Pair<>(placeName, userCurrentLocation);
        autocompleteMediatorLiveData.setValue(autocompleteResponse);
        Mockito.doReturn(autocompleteMediatorLiveData)
                .when(autocompleteRepository)
                .getAutocompleteMediatorLiveData();

        List<Photo> photosList = new ArrayList<>();
        photosList.add(
                new Photo(1080, new ArrayList<>(), "photoReference", 1920)
        );

        List<com.christophedurand.go4lunch.model.pojo.Restaurant> restaurantsList = new ArrayList<>();
        restaurantsList.add(
                new com.christophedurand.go4lunch.model.pojo.Restaurant(
                        "vicinity1",
                        new Geometry(new RestaurantLocation(31.0, 43.0)),
                        "icon1",
                        "name1",
                        new OpeningHours(true, null, null),
                        "placeId1",
                        5.0,
                        null
                )
        );

        NearbyRestaurantsResponse response = new NearbyRestaurantsResponse(restaurantsList);
        nearbyRestaurantsResponseLiveData.setValue(response);
        Mockito.doReturn(nearbyRestaurantsResponseLiveData)
                .when(nearbyRepository)
                .getNearbyRestaurantsResponseByRadiusLiveData(
                        "name1",
                        "restaurant",
                        "30.0,42.0",
                        "1000",
                        apiKey
                );

        List<User> usersList = new ArrayList<>();
        usersList.add(
                new User(
                        "uid",
                        "name",
                        "email",
                        "avatarURL",
                        new Restaurant(
                                "placeId1",
                                "name1",
                                "vicinity1"
                        ),
                        new ArrayList<>(),
                        true
                )
        );
        usersListLiveData.setValue(usersList);
        Mockito.doReturn(usersListLiveData)
                .when(userRepository)
                .fetchAllUsers();

        mapViewModel = new MapViewModel(application,
                                        nearbyRepository,
                                        permissionChecker,
                                        currentLocationRepository,
                                        placeNameRepository,
                                        autocompleteRepository,
                                        userRepository,
                                        firebaseAuth);
    }


    @Test
    public void nominal_case() throws InterruptedException {
        MapViewState mapViewState = LiveDataTestUtils.getOrAwaitValue(mapViewModel.getMapViewStateLiveData(), 1);
        assertEquals(1, mapViewState.getMapMarkersList().size());
    }

    @Test
    public void when_oneRestaurantHasNoPhotos_should_return_fake_photo_reference() throws InterruptedException {
        MapViewState mapViewState = LiveDataTestUtils.getOrAwaitValue(mapViewModel.getMapViewStateLiveData(), 1);
        assertNull(mapViewState.getMapMarkersList().get(0).getPhotoReference());
    }

}