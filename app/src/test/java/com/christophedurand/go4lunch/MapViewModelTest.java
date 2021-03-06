package com.christophedurand.go4lunch;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.model.pojo.Geometry;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.ui.mapView.MapViewModel;
import com.christophedurand.go4lunch.ui.mapView.MapViewState;

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
public class MapViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);

    private final CurrentLocationRepository currentLocationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final NearbyRepository nearbyRepository = Mockito.mock(NearbyRepository.class);

    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private final MutableLiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData = new MutableLiveData<>();

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

        List<Photo> photosList = new ArrayList<>();
        photosList.add(
                new Photo(1080, new ArrayList<>(), "photoReference", 1920)
        );

        List<Restaurant> restaurantsList = new ArrayList<>();
        restaurantsList.add(
                new Restaurant(
                        "vicinity",
                        new Geometry(new RestaurantLocation(39.0, 45.0)),
                        "icon",
                        "name",
                        new OpeningHours(true, null, null),
                        "placeId",
                        4.0,
                        photosList
                )
        );
        restaurantsList.add(
                new Restaurant(
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
                        "restaurant",
                        "30.0,42.0",
                        "1000",
                        apiKey
                );

        mapViewModel = new MapViewModel(application,
                                        nearbyRepository,
                                        currentLocationRepository
        );
    }


    @Test
    public void nominal_case() throws InterruptedException {
        // Given


        // When
        MapViewState mapViewState = LiveDataTestUtils.getOrAwaitValue(mapViewModel.getMapViewStateLiveData());

        // Then
        assertEquals(2, mapViewState.getMapMarkersList().size());
    }

    @Test
    public void when_oneRestaurantHasNoPhotos_should_return_fake_photo_reference() throws InterruptedException {
        // Given


        // When
        MapViewState mapViewState = LiveDataTestUtils.getOrAwaitValue(mapViewModel.getMapViewStateLiveData());

        // Then
        assertNull(mapViewState.getMapMarkersList().get(1).getPhotoReference());

    }

}