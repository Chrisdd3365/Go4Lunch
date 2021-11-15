package com.christophedurand.go4lunch.data.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.ui.workmatesView.Restaurant;
import com.christophedurand.go4lunch.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class UserRepository {

    private static final String COLLECTION_USERS = "users";
    private static UserRepository sUserRepository;


    private UserRepository() { }

    public static UserRepository getInstance() {
        if (sUserRepository == null) {
            sUserRepository = new UserRepository();
        }
        return sUserRepository;
    }


    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    @Nullable
    public FirebaseUser getFirebaseCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private String getCurrentUserUID() { return getFirebaseCurrentUser().getUid(); }

    public Task<Void> deleteCurrentUser(Context context) { return AuthUI.getInstance().delete(context); }

    public void createCurrentUser(Restaurant restaurant, List<String> favoriteRestaurantsIdsList, boolean hasSetNotifications) {
        FirebaseUser firebaseUser = getFirebaseCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            String avatarURL = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;

            User userToCreate = new User(uid, name, email, avatarURL, restaurant, favoriteRestaurantsIdsList, hasSetNotifications);

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

    public Task<User> getCurrentUser() {
        // Get the user from Firestore and cast it to a User model Object
        return getCurrentUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public LiveData<User> getCurrentUserLiveData() {
        final MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
        getCurrentUser().addOnSuccessListener(currentUser -> {
            currentUserMutableLiveData.setValue(currentUser);
        });
        return currentUserMutableLiveData;
    }

    private final MutableLiveData<Boolean> isJoiningLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsJoiningLiveData() {
        return isJoiningLiveData;
    }

    public void setCurrentUserIsJoining(String restaurantId, String chosenRestaurantName, String chosenRestaurantAddress) {
        getCurrentUser().addOnSuccessListener(currentUser -> {
            if ((currentUser.getRestaurant() != null && currentUser.getRestaurant().getId() != null)
                    && currentUser.getRestaurant().getId().contains(restaurantId)) {
                updateChosenRestaurant("", "", "", currentUser.getUid());
                isJoiningLiveData.setValue(false);
            } else {
                updateChosenRestaurant(restaurantId, chosenRestaurantName, chosenRestaurantAddress, currentUser.getUid());
                isJoiningLiveData.setValue(true);
            }
        });
    }

    private final MutableLiveData<Boolean> isFavoriteLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsFavoriteLiveData() {
        return isFavoriteLiveData;
    }
    public void setCurrentUserIsFavorite(String restaurantId) {
        getCurrentUser().addOnSuccessListener(currentUser -> {
            if (currentUser.getFavoriteRestaurantsIdsList() == null || !currentUser.getFavoriteRestaurantsIdsList().contains(restaurantId)) {
                updateFavoriteRestaurantsIdsList(restaurantId, currentUser.getUid());
                currentUser.getFavoriteRestaurantsIdsList().add(restaurantId);
                isFavoriteLiveData.setValue(true);
            } else {
                updateFavoriteRestaurantsIdsList(restaurantId, currentUser.getUid());
                currentUser.getFavoriteRestaurantsIdsList().remove(restaurantId);
                isFavoriteLiveData.setValue(false);
            }
        });
    }

    public void deleteCurrentUserFromFirestore() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            getUsersCollection().document(uid).delete();
        }
    }

    public void createUser(String userId, String userName, String email, String avatarURL, Restaurant restaurant, List<String> favoriteRestaurantsIdsList, boolean hasSetNotifications) {
        final User userToCreate = new User(userId, userName, email, avatarURL, restaurant, favoriteRestaurantsIdsList, hasSetNotifications);
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

    public LiveData<List<User>> fetchAllUsers() {
        final MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();

        getAllUsers().get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots == null) {
                return;
            }

            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                users.add(document.toObject(User.class));
            }
            allUsersMutableLiveData.setValue(users);
        });

        return allUsersMutableLiveData;
    }

    public Query getAllUsersListWithout(String currentUserId) {
        return getUsersCollection().whereNotEqualTo("uid", currentUserId);
    }

    public LiveData<List<User>> fetchWorkmatesList(String currentUserId) {
        final MutableLiveData<List<User>> filteredUsersListLiveData = new MutableLiveData<>(new ArrayList<>());

        getAllUsersListWithout(currentUserId).get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots == null) {
                return;
            }

            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                users.add(document.toObject(User.class));
            }
            filteredUsersListLiveData.setValue(users);
        });

        return filteredUsersListLiveData;
    }

    public Query getUsersFilteredListBy(String restaurantId) {
        return getUsersCollection().whereEqualTo("restaurant.id", restaurantId);
    }

    public LiveData<List<User>> fetchUsersFilteredList(String restaurantId) {
        final MutableLiveData<List<User>> filteredUsersListLiveData = new MutableLiveData<>(new ArrayList<>());

        getUsersFilteredListBy(restaurantId).get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots == null) {
                return;
            }

            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                users.add(document.toObject(User.class));
            }
            filteredUsersListLiveData.setValue(users);
        });

        return filteredUsersListLiveData;
    }

    private final MutableLiveData<Integer> numberOfWorkmatesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> getNumberOfWorkmatesMutableLiveData() {
        return numberOfWorkmatesMutableLiveData;
    }

    public void getNumberOfWorkmatesFor(String restaurantId) {
        getAllUsers().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> workmatesList = queryDocumentSnapshots.getDocuments();

            int numberOfWorkmates = 0;
            for (DocumentSnapshot workmate : workmatesList) {
                User user = workmate.toObject(User.class);
                if (user != null && user.getRestaurant() != null && user.getRestaurant().getId() != null && user.getRestaurant().getId().equals(restaurantId)) {
                    numberOfWorkmates += 1;
                }
                numberOfWorkmatesMutableLiveData.setValue(numberOfWorkmates);
            }
        });
    }

    public void deleteUser(String userId)  {
        getUsersCollection().document(userId).delete();
    }

    public void updateFavoriteRestaurantsIdsList(final String favoriteRestaurantId, final String userId) {
        getUserData(userId).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);

            if (user != null && user.getFavoriteRestaurantsIdsList().contains(favoriteRestaurantId)) {
                user.getFavoriteRestaurantsIdsList().remove(favoriteRestaurantId);
            } else {
                user.getFavoriteRestaurantsIdsList().add(favoriteRestaurantId);
            }

            getUsersCollection().document(userId).update("favoriteRestaurantsIdsList", user.getFavoriteRestaurantsIdsList());
        });
    }

    public void updateChosenRestaurant(final String chosenRestaurantId, final String chosenRestaurantName, final String chosenRestaurantAddress, final String userId) {
        getUserData(userId).addOnSuccessListener(documentSnapshot -> {
            getUsersCollection().document(userId).update("restaurant.id", chosenRestaurantId);
            getUsersCollection().document(userId).update("restaurant.name", chosenRestaurantName);
            getUsersCollection().document(userId).update("restaurant.address", chosenRestaurantAddress);
        });
    }

    public void updateHasSetNotifications(final boolean hasSetNotifications, final String userId) {
        getUserData(userId).addOnSuccessListener(documentSnapshot -> {
            getUsersCollection().document(userId).update("hasSetNotifications", hasSetNotifications);
        });
    }

}