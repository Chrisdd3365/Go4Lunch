package com.christophedurand.go4lunch.data.user;


import android.content.Context;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


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


    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private String getCurrentUserUID() {
        return getCurrentUser().getUid();
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) { return AuthUI.getInstance().delete(context); }


    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    public Query getFavoritesRestaurantsListForUser(String userId) {
        return getUsersCollection()
                .document(userId)
                .collection(COLLECTION_FAVORITES_RESTAURANTS)
                .orderBy("dateCreated")
                .limit(50);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String avatarURL = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            User userToCreate = new User(uid, name, email, avatarURL,true);

            Task<DocumentSnapshot> userData = getUserData();
            if (userData != null) {
                userData.addOnSuccessListener(documentSnapshot -> {
                    getUsersCollection().document(uid).set(userToCreate);
                });
            }
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            getUsersCollection().document(uid).delete();
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

}