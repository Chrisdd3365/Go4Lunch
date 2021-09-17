package com.christophedurand.go4lunch.data.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.Restaurant;
import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


public final class UserRepository {

    private static final String COLLECTION_USERS = "users";

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

    public void createCurrentUser(Restaurant restaurant, List<String> favoriteRestaurantsIdsList) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String avatarURL = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            User userToCreate = new User(uid, name, email, avatarURL, restaurant, favoriteRestaurantsIdsList);

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

    public void createUser(String userId, String userName, String email, String avatarURL, Restaurant restaurant, List<String> favoriteRestaurantsIdsList) {
        final User userToCreate = new User(userId, userName, email, avatarURL, restaurant, favoriteRestaurantsIdsList);
        Task<DocumentSnapshot> userData = getUserData(userId);
        if (userData != null) {
            userData.addOnSuccessListener(documentSnapshot -> {
                getUsersCollection().document(userId).set(userToCreate);
            })
                    .addOnFailureListener(e -> {
                        Log.d("Error", "Error writing document", e);
                    });
        }
    }

    public Task<DocumentSnapshot> getUserData(String userId) {
        return getUsersCollection().document(userId).get();
    }

    public Query getAllUsers() {
        return getUsersCollection().orderBy("name");
    }

    public Query getUsersFilteredListBy(String restaurantId) {
        return getUsersCollection().whereEqualTo("restaurant.id", restaurantId);
    }

    public void deleteUser(String userId)  {
        getUsersCollection().document(userId).delete();
    }

    public void updateFavoriteRestaurantsIdsList(final String favoriteRestaurantId, final String userId) {
        getUserData(userId).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);

            if (user.getFavoriteRestaurantsIdsList().contains(favoriteRestaurantId)) {
                user.getFavoriteRestaurantsIdsList().remove(favoriteRestaurantId);
            } else {
                user.getFavoriteRestaurantsIdsList().add(favoriteRestaurantId);
            }

            getUsersCollection().document(userId).update("favoriteRestaurantsIdsList", user.getFavoriteRestaurantsIdsList());
        });
    }

    public void updateChosenRestaurant(final String chosenRestaurantId, final String chosenRestaurantName, final String userId) {
        getUserData(userId).addOnSuccessListener(documentSnapshot -> {
            getUsersCollection().document(userId).update("restaurant.id", chosenRestaurantId);
            getUsersCollection().document(userId).update("restaurant.name", chosenRestaurantName);
        });
    }

}