package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class OpeningHours {

    @SerializedName("open_now")
    private final Boolean openNow;

    @SerializedName("periods")
    private final List<Period> periods;

    @SerializedName("weekday_text")
    private final List<String> weekdayText;


    public OpeningHours(Boolean openNow, List<Period> periods, List<String> weekdayText) {
        this.openNow = openNow;
        this.periods = periods;
        this.weekdayText = weekdayText;
    }


    public Boolean isOpenNow() {
        return openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }


    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                ", periods=" + periods +
                ", weekdayText=" + weekdayText +
                '}';
    }

}
