package com.christophedurand.go4lunch.model;


import androidx.annotation.Nullable;


public class User {

    private String uid;
    private String name;
    private String email;
    @Nullable
    private String avatarURL;


    public User() {

    }

    public User(String uid, String name, String email, @Nullable String avatarURL) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
    }

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
}
