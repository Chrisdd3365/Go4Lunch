package com.christophedurand.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.Objects;


public class User {

    private String uid = "";
    private String name = "";
    private String email = "";
    @Nullable
    private String avatarURL = "";
    @Nullable
    private String restaurantName = "";
    @Nullable
    private String restaurantId = "";


    public User(String uid, String name, String email, @Nullable String avatarURL, @Nullable String restaurantName, @Nullable String restaurantId) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
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
    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(avatarURL, user.avatarURL) && Objects.equals(restaurantName, user.restaurantName) && Objects.equals(restaurantId, user.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, email, avatarURL, restaurantName, restaurantId);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", userRestaurantName='" + restaurantName + '\'' +
                ", userRestaurantId='" + restaurantId + '\'' +
                '}';
    }

}
