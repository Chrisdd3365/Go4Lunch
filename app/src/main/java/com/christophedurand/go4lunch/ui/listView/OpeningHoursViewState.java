package com.christophedurand.go4lunch.ui.listView;


import com.christophedurand.go4lunch.model.pojo.Period;

import java.util.List;


class OpeningHoursViewState {

    private boolean openNow;
    private List<Period> periodsList;


    public OpeningHoursViewState(boolean openNow, List<Period> periodsList) {
        this.openNow = openNow;
        this.periodsList = periodsList;
    }


    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriodsList() {
        return periodsList;
    }

    public void setPeriodsList(List<Period> periodsList) {
        this.periodsList = periodsList;
    }


    @Override
    public String toString() {
        return "OpeningHoursViewState{" +
                "openNow=" + openNow +
                ", periodsList=" + periodsList +
                '}';
    }

}
