package com.christophedurand.go4lunch.model;

import android.content.Context;

import com.christophedurand.go4lunch.data.user.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;


public class UserManager {

    private static volatile UserManager instance;
    private UserRepository userRepository;


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

    public void createCurrentUser(String restaurantName, String restaurantId) {
        userRepository.createCurrentUser(restaurantName, restaurantId);
    }

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getCurrentUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> deleteCurrentUser(Context context) {
        return userRepository.deleteCurrentUser(context).addOnCompleteListener(task -> {
            userRepository.deleteCurrentUserFromFirestore();
        });
    }

    public void createUser(String userId, String userName, String email, String avatarURL, String restaurantName, String restaurantId) {
        userRepository.createUser(userId, userName, email, avatarURL, restaurantName, restaurantId);
    }

    public void getUser(String userId) {
        userRepository.getUser(userId);
    }

    public Query getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }

    public void getFavoritesList(String userId) {
        userRepository.getFavoritesList(userId);
    }

    public void addRestaurantToFavoritesList(String userId, String restaurantId) {
        userRepository.addRestaurantToFavoritesList(userId, restaurantId);
    }

    public void deleteRestaurantFromFavoritesList(String userId, String restaurantId) {
        userRepository.deleteRestaurantFromFavoritesList(userId, restaurantId);
    }

}