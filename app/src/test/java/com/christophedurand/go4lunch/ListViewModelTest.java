package com.christophedurand.go4lunch;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.autocomplete.AutocompleteRepository;
import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.data.placeName.PlaceNameRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.pojo.Geometry;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.Open;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Period;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.ui.listView.ListViewModel;
import com.christophedurand.go4lunch.ui.listView.ListViewState;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.API_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import kotlin.Pair;


@RunWith(MockitoJUnitRunner.class)
public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);

    private final CurrentLocationRepository currentLocationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final NearbyRepository nearbyRepository = Mockito.mock(NearbyRepository.class);
    private final PlaceNameRepository placeNameRepository = Mockito.mock(PlaceNameRepository.class);
    private final DetailsRepository detailsRepository = Mockito.mock(DetailsRepository.class);
    private final AutocompleteRepository autocompleteRepository = Mockito.mock(AutocompleteRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<String> placeNameMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private final MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersListLiveData = new MutableLiveData<>();

    private ListViewModel listViewModel;


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
                new Photo(1080, new ArrayList<>(), "photoReference1", 1920)
        );
        photosList.add(
                new Photo(1080, new ArrayList<>(), "photoReference2", 1920)
        );

        List<Period> periodsList = new ArrayList<>();
        periodsList.add(
                new Period(
                        new Open(0, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(1, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(2, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(3, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(4, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(5, "Open until 7PM")
                )
        );
        periodsList.add(
                new Period(
                        new Open(6, "Open until 7PM")
                )
        );

        List<String> weekdayText = new ArrayList<>();
        weekdayText.add("Sunday");
        weekdayText.add("Monday");
        weekdayText.add("Tuesday");
        weekdayText.add("Wednesday");
        weekdayText.add("Thursday");
        weekdayText.add("Friday");
        weekdayText.add("Saturday");

        List<Restaurant> restaurantsList = new ArrayList<>();
        restaurantsList.add(
                new Restaurant(
                        "vicinity1",
                        new Geometry(new RestaurantLocation(31.0, 43.0)),
                        "icon1",
                        "name1",
                        new OpeningHours(true, periodsList, weekdayText),
                        "placeId",
                        4.2,
                        photosList
                )
        );

        NearbyRestaurantsResponse nearbyRestaurantsResponse = new NearbyRestaurantsResponse(restaurantsList);
        nearbyRestaurantsResponseLiveData.setValue(nearbyRestaurantsResponse);
        Mockito.doReturn(nearbyRestaurantsResponseLiveData)
                .when(nearbyRepository)
                .getNearbyRestaurantsResponseByRadiusLiveData(
                        "name1",
                        "restaurant",
                        "30.0,42.0",
                        "1000",
                        API_KEY
                );

        RestaurantDetails restaurantDetails = new RestaurantDetails(
                "vicinity1",
                new Geometry(new RestaurantLocation(31.0, 43.0)),
                "icon1",
                "internationalPhoneNumber",
                "name1",
                new OpeningHours(true, periodsList, weekdayText),
                "placeId",
                4.2,
                "reference",
                "url",
                "website",
                photosList
        );

        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse(restaurantDetails);
        restaurantDetailsMutableLiveData.setValue(restaurantDetailsResponse);
        Mockito.doReturn(restaurantDetailsMutableLiveData)
                .when(detailsRepository)
                .getRestaurantDetailsMutableLiveData(
                        "placeId"
                );

        List<User> usersList = new ArrayList<>();
        usersList.add(
                new User(
                        "uid",
                        "name",
                        "email",
                        "avatarURL",
                        new com.christophedurand.go4lunch.ui.workmatesView.Restaurant(
                                "placeId",
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

        listViewModel = new ListViewModel(
                application,
                permissionChecker,
                currentLocationRepository,
                placeNameRepository,
                nearbyRepository,
                detailsRepository,
                autocompleteRepository,
                userRepository,
                firebaseAuth
        );
    }


    @Test
    public void nominal_case() throws InterruptedException {
        ListViewState listViewState = LiveDataTestUtils.getOrAwaitValue(listViewModel.getListViewStateMediatorLiveData(), 1);
        assertEquals(0, listViewState.getRestaurantViewStatesList().size());
    }

    @Test
    public void when_currentLocationIsNull_should_return_no_data() {
        currentLocationLiveData.setValue(null);
        ListViewState listViewState = LiveDataTestUtils.observeForTesting(listViewModel.getListViewStateMediatorLiveData());
        assertNull(listViewState);
    }

}
