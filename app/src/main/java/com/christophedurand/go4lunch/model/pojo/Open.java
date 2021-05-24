package com.christophedurand.go4lunch.model.pojo;


import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class Open {

    @SerializedName("day")
    private final Integer day;

    @SerializedName("time")
    private final String time;


    public Open(Integer day, String time) {
        this.day = day;
        this.time = time;
    }


    public int getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Open open = (Open) o;
        return Objects.equals(day, open.day) &&
                Objects.equals(time, open.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time);
    }

    @Override
    public String toString() {
        return "Open{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }

}
