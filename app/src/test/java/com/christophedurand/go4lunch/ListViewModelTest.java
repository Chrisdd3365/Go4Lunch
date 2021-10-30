package com.christophedurand.go4lunch;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(MockitoJUnitRunner.class)
public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    private final Application application = Mockito.mock(Application.class);

    private final CurrentLocationRepository currentLocationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final NearbyRepository nearbyRepository = Mockito.mock(NearbyRepository.class);
    private final DetailsRepository detailsRepository = Mockito.mock(DetailsRepository.class);

    private final MutableLiveData<String> placeNameMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<RestaurantDetailsResponse> restaurantDetailsMutableLiveData = new MutableLiveData<>();

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
        restaurantsList.add(
                new Restaurant(
                        "vicinity2",
                        new Geometry(new RestaurantLocation(33.0, 45.0)),
                        "icon2",
                        "name2",
                        new OpeningHours(false, null, null),
                        "placeId",
                        3.2,
                        null
                )
        );
        restaurantsList.add(
                new Restaurant(
                        "vicinity3",
                        new Geometry(new RestaurantLocation(37.0, 47.0)),
                        "icon3",
                        "name3",
                        null,
                        "placeId",
                        2.2,
                        photosList
                )
        );
        restaurantsList.add(
                new Restaurant(
                        "vicinity4",
                        new Geometry(new RestaurantLocation(39.0, 69.0)),
                        "icon4",
                        "name4",
                        null,
                        "placeId",
                        1.2,
                        photosList
                )
        );
        restaurantsList.add(
                new Restaurant(
                        "vicinity5",
                        new Geometry(new RestaurantLocation(78.0, 109.0)),
                        "icon5",
                        "name5",
                        null,
                        "placeId",
                        0.9,
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
                        apiKey
                );

        RestaurantDetails restaurantDetails = new RestaurantDetails(
                "formattedAddress",
                new Geometry(new RestaurantLocation(31.0, 43.0)),
                "icon",
                "internationalPhoneNumber",
                "name",
                new OpeningHours(true, null, null),
                "placeId",
                3.0,
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

        listViewModel = new ListViewModel(
                application,
                currentLocationRepository,
                nearbyRepository,
                detailsRepository
        );
    }


    @Test
    public void nominal_case() throws InterruptedException {
        // Given

        // When
        ListViewState listViewState = LiveDataTestUtils.getOrAwaitValue(listViewModel.getListViewStateMediatorLiveData(), 1);

        // Then
        assertEquals(5, listViewState.getRestaurantViewStatesList().size());
    }


    @Test
    public void when_currentLocationIsNull_should_return_no_data() throws InterruptedException {
        // Given
        currentLocationLiveData.setValue(null);

        // When
        ListViewState listViewState = LiveDataTestUtils.observeForTesting(listViewModel.getListViewStateMediatorLiveData());

        // Then
        assertNull(listViewState);
    }

}
