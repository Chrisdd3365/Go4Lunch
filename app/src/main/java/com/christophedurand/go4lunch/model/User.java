package com.christophedurand.go4lunch.model;


import androidx.annotation.Nullable;


public class User {

    private final String uid;
    private final String name;
    private final String email;
    @Nullable
    private final String avatarURL;


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
