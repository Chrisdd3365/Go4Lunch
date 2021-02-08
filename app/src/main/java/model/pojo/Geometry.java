package model;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    //-- PROPERTIES
    @SerializedName("location")
    public Location location;


    //-- CONSTRUCTOR
    public Geometry(Location location) {
        this.location = location;
    }


    //-- GETTERS && SETTERS
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                '}';
    }

}
