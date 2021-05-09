package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class OpeningHours {

    @SerializedName("open_now")
    public boolean openNow;
    @SerializedName("periods")
    private List<Period> periods;
    @SerializedName("weekday_text")
    private List<String> weekdayText;


    public OpeningHours(boolean openNow, List<Period> periods, List<String> weekdayText) {
        this.openNow = openNow;
        this.periods = periods;
        this.weekdayText = weekdayText;
    }


    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
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
