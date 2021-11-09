package com.christophedurand.go4lunch;

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.ui.workmatesView.WorkmatesViewModel;
import com.christophedurand.go4lunch.ui.workmatesView.WorkmatesViewState;
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
public class WorkmatesViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<List<User>> workmatesListLiveData = new MutableLiveData<>();

    private WorkmatesViewModel workmatesViewModel;


    @Before
    public void setUp() {
        List<User> usersList = new ArrayList<>();
        usersList.add(
                new User(
                        "uid1",
                        "name1",
                        "email1",
                        "avatarURL1",
                        new com.christophedurand.go4lunch.ui.workmatesView.Restaurant(
                                "placeId1",
                                "name1",
                                "vicinity1"
                        ),
                        new ArrayList<>(),
                        true
                )
        );
        usersList.add(
                new User(
                        firebaseAuth.getUid(),
                        "name2",
                        "email2",
                        "avatarURL2",
                        new com.christophedurand.go4lunch.ui.workmatesView.Restaurant(
                                "placeId2",
                                "name2",
                                "vicinity2"
                        ),
                        new ArrayList<>(),
                        true
                )
        );
        workmatesListLiveData.setValue(usersList);
        Mockito.doReturn(workmatesListLiveData)
                .when(userRepository)
                .fetchWorkmatesList(firebaseAuth.getUid());

        workmatesViewModel = new WorkmatesViewModel(
                application,
                userRepository,
                firebaseAuth
        );
    }


    @Test
    public void nominal_case() throws InterruptedException {
        WorkmatesViewState workmatesViewState = LiveDataTestUtils.getOrAwaitValue(workmatesViewModel.getWorkmatesViewStateMediatorLiveData(), 1);
        assertEquals(2, workmatesViewState.getWorkmatesList().size());
    }

}
