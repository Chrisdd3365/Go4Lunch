package com.christophedurand.go4lunch;

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.details.DetailsRepository;
import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.pojo.Geometry;
import com.christophedurand.go4lunch.model.pojo.OpeningHours;
import com.christophedurand.go4lunch.model.pojo.Photo;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
import com.christophedurand.go4lunch.model.pojo.RestaurantLocation;
import com.christophedurand.go4lunch.ui.detailsView.DetailsViewState;
import com.christophedurand.go4lunch.ui.detailsView.DetailsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class DetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);

    private final DetailsRepository detailsRepository = Mockito.mock(DetailsRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<RestaurantDetailsResponse> restaurantDetailsResponseLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<User>> usersFilteredListLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> joiningMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> favoriteMutableLiveData = new MutableLiveData<>();

    private DetailsViewModel mDetailsViewModel;


    @Before
    public void setUp() {

        String restaurantId = "placeId";

        List<Photo> photosList = new ArrayList<>();
        photosList.add(
                new Photo(1080, new ArrayList<>(), "photoReference1", 1920)
        );
        photosList.add(
                new Photo(1080, new ArrayList<>(), "photoReference2", 1920)
        );

        RestaurantDetails restaurantDetails = new RestaurantDetails(
                "formattedAddress",
                new Geometry(new RestaurantLocation(31.0, 43.0)),
                "icon1",
                "internationalPhoneNumber",
                "name1",
                new OpeningHours(true, null, null),
                "placeId",
                4.2,
                "reference",
                "url",
                "website",
                photosList
        );

        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse(restaurantDetails);
        restaurantDetailsResponseLiveData.setValue(restaurantDetailsResponse);
        Mockito.doReturn(restaurantDetailsResponseLiveData)
                .when(detailsRepository)
                .getRestaurantDetailsMutableLiveData(
                        restaurantId
                );

        List<User> usersList = new ArrayList<>();
        usersList.add(
                new User(
                        "uid",
                        "name",
                        "email",
                        "avatarURL",
                        new com.christophedurand.go4lunch.ui.workmatesView.Restaurant(
                                "placeId1",
                                "name1",
                                "vicinity1"
                        ),
                        new ArrayList<>(),
                        true
                )
        );
        usersFilteredListLiveData.setValue(usersList);
        Mockito.doReturn(usersFilteredListLiveData)
                .when(userRepository)
                .fetchUsersFilteredList(restaurantId);

        final Boolean isJoining = false;
        joiningMutableLiveData.setValue(isJoining);
        Mockito.doReturn(joiningMutableLiveData)
                .when(userRepository)
                .getIsJoiningLiveData();

        final Boolean isFavorite = true;
        favoriteMutableLiveData.setValue(isFavorite);
        Mockito.doReturn(favoriteMutableLiveData)
                .when(userRepository)
                .getIsFavoriteLiveData();

        mDetailsViewModel = new DetailsViewModel(
                application,
                detailsRepository,
                userRepository,
                firebaseAuth,
                restaurantId
        );
    }


    @Test
    public void nominal_case() throws InterruptedException {
        DetailsViewState detailsViewState = LiveDataTestUtils.getOrAwaitValue(mDetailsViewModel.getDetailsViewStateLiveData(), 1);
        assertEquals("placeId", detailsViewState.getRestaurantId());
    }

}
