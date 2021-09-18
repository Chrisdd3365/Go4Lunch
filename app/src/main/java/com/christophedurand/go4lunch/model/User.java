package com.christophedurand.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class User {

    private String uid = "";
    private String name = "";
    private String email = "";
    @Nullable
    private String avatarURL = "";
    @Nullable
    private Restaurant restaurant = new Restaurant("", "", "");
    private List<String> favoriteRestaurantsIdsList = new ArrayList<>();


    public User(String uid, String name, String email, @Nullable String avatarURL, @Nullable Restaurant restaurant, List<String> favoriteRestaurantsIdsList) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.restaurant = restaurant;
        this.favoriteRestaurantsIdsList = favoriteRestaurantsIdsList;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(avatarURL, user.avatarURL) && Objects.equals(restaurant, user.restaurant) && Objects.equals(favoriteRestaurantsIdsList, user.favoriteRestaurantsIdsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, email, avatarURL, restaurant, favoriteRestaurantsIdsList);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", restaurant=" + restaurant +
                ", favoriteRestaurantsIdsList=" + favoriteRestaurantsIdsList +
                '}';
    }

}
