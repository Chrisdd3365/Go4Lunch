package com.christophedurand.go4lunch.ui.workmatesView;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.data.nearby.NearbyRepository;
import com.christophedurand.go4lunch.data.permissionChecker.PermissionChecker;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class WorkmatesViewModel extends AndroidViewModel {

    private final MediatorLiveData<WorkmatesViewState> workmatesViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<WorkmatesViewState> getWorkmatesViewStateMediatorLiveData() {
        return workmatesViewStateMediatorLiveData;
    }

    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final CurrentLocationRepository currentLocationRepository;

    private LifecycleOwner _owner;


    public WorkmatesViewModel(@NonNull Application application,
                              @NonNull PermissionChecker permissionChecker,
                              @NonNull CurrentLocationRepository currentLocationRepository,
                              NearbyRepository nearbyRepository) {
        super(application);

        this.permissionChecker = permissionChecker;
        this.currentLocationRepository = currentLocationRepository;

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
                                "",
                                "restaurant",
                                location.getLatitude() + "," + location.getLongitude(),
                                "1000",
                                apiKey)
                );

        workmatesViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, nearbyRestaurantsResponseLiveData.getValue())
        );

        workmatesViewStateMediatorLiveData.addSource(nearbyRestaurantsResponseLiveData, nearbyRestaurantsResponse ->
                combine(locationLiveData.getValue(), nearbyRestaurantsResponse)
        );

    }

    private void combine(@Nullable Location currentLocation,
                         @Nullable NearbyRestaurantsResponse response) {

        if (currentLocation == null || response == null) {
            return;
        }

        HashMap<Integer, Restaurant> map = new HashMap<>();

        for (int i=0; i<response.getResults().size(); i++) {
            String restaurantId = response.getResults().get(i).getPlaceId();
            String restaurantName = response.getResults().get(i).getName();
            String restaurantAddress = response.getResults().get(i).getVicinity();

            map.put(i, new Restaurant(restaurantId, restaurantName, restaurantAddress));
        }

        workmatesViewStateMediatorLiveData.setValue(new WorkmatesViewState(createWorkmatesDataSource(map)));
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (!permissionChecker.hasLocationPermission()) {
            currentLocationRepository.stopLocationRequest();
        } else {
            currentLocationRepository.initCurrentLocationUpdate();
        }
    }

    private FirestoreRecyclerOptions<User> createWorkmatesDataSource(HashMap<Integer, Restaurant> map) {
        //createUsers(map);
        Query query = UserManager.getInstance().getAllUsers();
        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).setLifecycleOwner(_owner).build();
    }

    private void createUsers(HashMap<Integer, Restaurant> map) {
        UserManager.getInstance().createUser("0", "Batman", "batman@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("1", "Superman", "superman@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("2", "Iron Man", "iron-man@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("3", "Spiderman", "spiderman@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("4", "Wonder Woman", "wonder-woman@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("5", "Aquaman", "aquaman@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("6", "Obi-Wan", "obi-wan@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("7", "Dark Vador", "dark.vador@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("8", "Son Goku", "songoku@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
        UserManager.getInstance().createUser("9", "Vegeta", "vegeta@gmail.com", "", map.get(new Random().nextInt(map.size())), new ArrayList<>(), false);
    }

    public void getLifecycleOwner(LifecycleOwner owner) {
        _owner = owner;
    }

}