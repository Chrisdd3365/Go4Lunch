package model.pojo;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    //-- PROPERTIES
    @SerializedName("open_now")
    public boolean openNow;


    //-- CONSTRUCTOR
    public OpeningHours(boolean openNow) {
        this.openNow = openNow;
    }


    //-- GETTERS && SETTERS
    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }


    //-- METHODS
    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                '}';
    }
}
