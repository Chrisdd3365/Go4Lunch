package com.christophedurand.go4lunch.data.user;

import android.content.Context;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.Restaurant;
import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public final class UserRepository {

    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_FAVORITES_RESTAURANTS = "favoritesRestaurants";

    private static volatile UserRepository instance;


    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }


    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }


    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private String getCurrentUserUID() { return getCurrentUser().getUid(); }

    public Task<Void> deleteCurrentUser(Context context) { return AuthUI.getInstance().delete(context); }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public void createCurrentUser(String restaurantName, String restaurantId) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String avatarURL = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            User userToCreate = new User(uid, name, email, avatarURL, restaurantName, restaurantId);

            Task<DocumentSnapshot> userData = getCurrentUserData();
            if (userData != null) {
                userData.addOnSuccessListener(documentSnapshot -> {
                    getUsersCollection().document(uid).set(userToCreate);
                });
            }
        }
    }

    public Task<DocumentSnapshot> getCurrentUserData() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public void deleteCurrentUserFromFirestore() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            getUsersCollection().document(uid).delete();
        }
    }

    public Task<Void> createUser(String userId, String userName, String email, String avatarURL, String restaurantName, String restaurantId) {
        final User userToCreate = new User(userId, userName, email, avatarURL, restaurantName, restaurantId);
        return getUsersCollection().document(userId).set(userToCreate);
    }

    public Task<DocumentSnapshot> getUser(String userId) {
        return getUsersCollection().document(userId).get();
    }

    public Query getAllUsers() {
        return getUsersCollection().orderBy("name");
    }

    public Task<Void> deleteUser(String userId)  {
        return getUsersCollection().document(userId).delete();
    }

    public Task<QuerySnapshot> getFavoritesList(String userId) {
        return getUsersCollection().document(userId).collection(COLLECTION_FAVORITES_RESTAURANTS).get();
    }

    public Task<Void> addRestaurantToFavoritesList(String userId, String restaurantId) {
        Restaurant favoriteRestaurant = new Restaurant(restaurantId);
        return getUsersCollection().document(userId).collection(COLLECTION_FAVORITES_RESTAURANTS).document(restaurantId).set(favoriteRestaurant);
    }

    public Task<Void> deleteRestaurantFromFavoritesList(String userId, String restaurantId) {
        return getUsersCollection().document(userId).collection(COLLECTION_FAVORITES_RESTAURANTS).document(restaurantId).delete();
    }

}