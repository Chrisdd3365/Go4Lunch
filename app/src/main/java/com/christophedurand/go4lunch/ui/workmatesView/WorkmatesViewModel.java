package com.christophedurand.go4lunch.ui.workmatesView;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;

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
import com.christophedurand.go4lunch.model.Restaurant;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.UserManager;
import com.christophedurand.go4lunch.model.pojo.NearbyRestaurantsResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Random;


public class WorkmatesViewModel extends AndroidViewModel {

    private final MediatorLiveData<WorkmatesViewState> workmatesViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<WorkmatesViewState> getWorkmatesViewStateMediatorLiveData() {
        return workmatesViewStateMediatorLiveData;
    }

    private final LifecycleOwner owner;


    public WorkmatesViewModel(@NonNull Application application,
                              CurrentLocationRepository currentLocationRepository,
                              NearbyRepository nearbyRepository,
                              LifecycleOwner owner) {
        super(application);

        this.owner = owner;

        currentLocationRepository.initCurrentLocationUpdate();

        LiveData<Location> locationLiveData = currentLocationRepository.getCurrentLocationLiveData();

        LiveData<NearbyRestaurantsResponse> nearbyRestaurantsResponseLiveData =
                Transformations.switchMap(
                        locationLiveData,
                        location -> nearbyRepository.getNearbyRestaurantsResponseByRadiusLiveData(
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

            map.put(i, new Restaurant(restaurantId, restaurantName));
        }

        workmatesViewStateMediatorLiveData.setValue(new WorkmatesViewState(createWorkmatesDataSource(map)));
    }


    private FirestoreRecyclerOptions<User> createWorkmatesDataSource(HashMap<Integer, Restaurant> map) {
        //createUsers(map);
        Query query = UserManager.getInstance().getAllUsers();
        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).setLifecycleOwner(owner).build();
    }

//    private void createUsers(HashMap<Integer, Restaurant> map) {
//        UserManager.getInstance().createUser("0", "Batman", "batman@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("1", "Superman", "superman@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("2", "Iron Man", "iron-man@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("3", "Spiderman", "spiderman@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("4", "Wonder Woman", "wonder-woman@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("5", "Aquaman", "aquaman@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("6", "Obi-Wan", "obi-wan@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("7", "Dark Vador", "dark.vador@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("8", "Son Goku", "songoku@gmail.com", "", map.get(new Random().nextInt(map.size())));
//        UserManager.getInstance().createUser("9", "Vegeta", "vegeta@gmail.com", "", map.get(new Random().nextInt(map.size())));
//    }

    public void onCleared() {
        workmatesViewStateMediatorLiveData.removeObservers(owner);
    }

}