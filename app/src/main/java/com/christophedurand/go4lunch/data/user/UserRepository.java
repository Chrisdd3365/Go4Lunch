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

public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";

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


    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String avatarURL = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            User userToCreate = new User(uid, name, email, avatarURL);

            Task<DocumentSnapshot> userData = getUserData();
            userData.addOnSuccessListener(documentSnapshot -> {
                getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        } else{
            return null;
        }
    }

}