package com.christophedurand.go4lunch;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.model.pojo.Geometry;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.ui.listView.ListViewModel;
import com.christophedurand.go4lunch.ui.listView.ListViewState;
import com.christophedurand.go4lunch.ui.listView.RestaurantViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;
import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    private final Application application = Mockito.mock(Application.class);

    private final CurrentLocationRepository currentLocationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final NearbyRepository nearbyRepository = Mockito.mock(NearbyRepository.class);
    private final DetailsRepository detailsRepository = Mockito.mock(DetailsRepository.class);

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

        List<Restaurant> restaurantsList = new ArrayList<>();
        restaurantsList.add(
                new Restaurant(
                        "vicinity1",
                        new Geometry(new RestaurantLocation(31.0, 43.0)),
                        "icon1",
                        "name1",
                        new OpeningHours(true, null, null),
                        "placeId1",
                        4.0,
                        photosList
                )
        );
        restaurantsList.add(
                new Restaurant(
                        "vicinity2",
                        new Geometry(new RestaurantLocation(33.0, 45.0)),
                        "icon2",
                        "name2",
                        new OpeningHours(true, null, null),
                        "placeId2",
                        3.2,
                        photosList
                )
        );

        NearbyRestaurantsResponse nearbyRestaurantsResponse = new NearbyRestaurantsResponse(restaurantsList);
        nearbyRestaurantsResponseLiveData.setValue(nearbyRestaurantsResponse);
        Mockito.doReturn(nearbyRestaurantsResponseLiveData)
                .when(nearbyRepository)
                .getNearbyRestaurantsResponseByRadiusLiveData(
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
        ListViewState listViewState = LiveDataTestUtils.getOrAwaitValue(listViewModel.getListViewStateMediatorLiveData());

        // Then
        assertEquals(1, listViewState.getRestaurantViewStatesList().size());
    }


    @Test
    public void when_restaurantDetailsResponseIsNotAvailable_should_get_restaurant_details_from_details_repository() throws InterruptedException {
        // Given
        List<RestaurantViewState> restaurantViewStatesList = new ArrayList<>();

        int i;
        for (i = 0; i < 3; i++) {
            RestaurantViewState restaurantViewState = new RestaurantViewState(
                    "7, rue auguste vitu" + i,
                    "100m",
                    "Chez Christophe" + i,
                    "placeId" + i,
                    5.0 - 1,
                    "photoReference" + i,
                    "OpeningHours" + i
                    );
            restaurantViewStatesList.add(restaurantViewState);
        }

        // When
        ListViewState listViewState = LiveDataTestUtils.getOrAwaitValue(listViewModel.getListViewStateMediatorLiveData());

        // Then
        assertEquals(2, listViewState.getRestaurantViewStatesList().size());
    }

    @Test
    public void when_restaurantDetailsResponseIsAvailable_should_add_restaurant_view_state_to_list_if_its_not_already_queried() throws InterruptedException {
        // Given
        List<RestaurantViewState> restaurantViewStatesList = new ArrayList<>();
        List<String> alreadyQueriedPlaceIds = new ArrayList<>();

        int i;
        for (i = 0; i < 3; i++) {
            if (!alreadyQueriedPlaceIds.contains("placeId" + i)) {
                alreadyQueriedPlaceIds.add("placeId" + i);

                RestaurantViewState restaurantViewState = new RestaurantViewState(
                        "7, rue auguste vitu" + i,
                        "100m",
                        "Chez Christophe" + i,
                        "placeId" + i,
                        5.0 - 1,
                        "photoReference" + i,
                        "OpeningHours" + i
                );
                restaurantViewStatesList.add(restaurantViewState);
            }
        }

        // When
        ListViewState listViewState = LiveDataTestUtils.getOrAwaitValue(listViewModel.getListViewStateMediatorLiveData());

        // Then
        assertEquals(2, listViewState.getRestaurantViewStatesList().size());

    }

}
