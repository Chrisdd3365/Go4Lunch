package com.christophedurand.go4lunch.model.pojo;


import com.google.gson.annotations.SerializedName;


public class Open {

    @SerializedName("day")
    private int day;

    @SerializedName("time")
    private String time;


    public Open(int day, String time) {
        this.day = day;
        this.time = time;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "Open{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }

}
