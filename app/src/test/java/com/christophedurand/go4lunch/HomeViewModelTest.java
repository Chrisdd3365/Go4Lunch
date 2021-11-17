package com.christophedurand.go4lunch;

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.ui.homeView.HomeViewModel;
import com.christophedurand.go4lunch.ui.homeView.HomeViewState;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;


@RunWith(MockitoJUnitRunner.class)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();

    private final LiveData<String> chosenRestaurantIdLiveData = new MutableLiveData<>();

    private HomeViewModel homeViewModel;


    @Test
    public void nominal_case() throws InterruptedException {
        User currentUser = new User(
                "AkiSPwmQB5cDFZ81PQ2HvZ1QeSK2",
                "Christophe DURAND",
                "cdurand.dev@gmail.com",
                "https://lh3.googleusercontent.com/a/AATXAJzlqaE2i2Ween-kxTAWhPLzewnZK0Q0MwKQf22r=s96-c",
                new com.christophedurand.go4lunch.ui.workmatesView.Restaurant(
                        "",
                        "",
                        ""
                ),
                new ArrayList<>(),
                false
        );
        currentUserLiveData.setValue(currentUser);
        Mockito.doReturn(currentUserLiveData)
                .when(userRepository)
                .getCurrentUserLiveData();

        String restaurantId = "restaurantId";
        chosenRestaurantIdLiveData.setValue(restaurantId)
        Mockito.doReturn(chosenRestaurantIdLiveData)
                .when(userRepository)
                .getChosenRestaurantIdLiveData();

        homeViewModel = new HomeViewModel(
                application,
                userRepository
        );

        HomeViewState homeViewState = LiveDataTestUtils.getOrAwaitValue(homeViewModel.getHomeViewStateMediatorLiveData(), 1);
        assertEquals(currentUser, homeViewState.getCurrentUser());
    }

}

