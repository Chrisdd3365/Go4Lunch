package com.christophedurand.go4lunch.ui.workmatesView;

import android.content.Context;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import java.util.List;


public class UserManager {

    private static volatile UserManager instance;
    private final UserRepository userRepository;


    private UserManager() {
        userRepository = UserRepository.getInstance();
    }


    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }


    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public void createCurrentUser(Restaurant restaurant, List<String> favoriteRestaurantsIdsList, boolean hasSetNotifications) {
        userRepository.createCurrentUser(restaurant, favoriteRestaurantsIdsList, hasSetNotifications);
    }

    public Task<User> getCurrentUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getCurrentUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> deleteCurrentUser(Context context) {
        return userRepository.deleteCurrentUser(context).addOnCompleteListener(task -> {
            userRepository.deleteCurrentUserFromFirestore();
        });
    }

    public void createUser(String userId, String userName, String email, String avatarURL, Restaurant restaurant, List<String> favoriteRestaurantsIdsList, boolean hasSetNotifications) {
        userRepository.createUser(userId, userName, email, avatarURL, restaurant, favoriteRestaurantsIdsList, hasSetNotifications);
    }

    public void getUser(String userId) {
        userRepository.getUserData(userId);
    }

    public Query getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Query getUsersFilteredListBy(String restaurantId) {
        return userRepository.getUsersFilteredListBy(restaurantId);
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }

    public void updateFavoriteRestaurantsIdsList(final String favoriteRestaurantId, final String userId) {
        userRepository.updateFavoriteRestaurantsIdsList(favoriteRestaurantId, userId);
    }

    public void updateChosenRestaurant(final String chosenRestaurantId, final String chosenRestaurantName, final String chosenRestaurantAddress, final String userId) {
        userRepository.updateChosenRestaurant(chosenRestaurantId, chosenRestaurantName, chosenRestaurantAddress, userId);
    }

    public void updateHasSetNotifications(final boolean hasSetNotifications, final String userId) {
        userRepository.updateHasSetNotifications(hasSetNotifications, userId);
    }

}