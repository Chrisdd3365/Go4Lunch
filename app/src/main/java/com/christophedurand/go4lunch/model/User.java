package com.christophedurand.go4lunch.model;

import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.ui.workmatesView.Restaurant;

import java.util.ArrayList;
import java.util.List;


public class User {

    private String uid = "";
    private String name = "";
    private String email = "";
    @Nullable
    private String avatarURL = "";
    @Nullable
    private Restaurant restaurant = new Restaurant("", "", "");
    private List<String> favoriteRestaurantsIdsList = new ArrayList<>();
    private boolean hasSetNotifications;

    public User(String uid,
                String name,
                String email,
                @Nullable String avatarURL,
                @Nullable Restaurant restaurant,
                List<String> favoriteRestaurantsIdsList,
                boolean hasSetNotifications) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.restaurant = restaurant;
        this.favoriteRestaurantsIdsList = favoriteRestaurantsIdsList;
        this.hasSetNotifications = hasSetNotifications;
    }

    public User() { }


    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getAvatarURL() {
        return avatarURL;
    }

    @Nullable
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<String> getFavoriteRestaurantsIdsList() {
        return favoriteRestaurantsIdsList;
    }

    public boolean isHasSetNotifications() {
        return hasSetNotifications;
    }

}
