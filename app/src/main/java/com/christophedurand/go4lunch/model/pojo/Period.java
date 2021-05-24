package com.christophedurand.go4lunch.model.pojo;


import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class Period {

    @SerializedName("open")
    private final Open open;


    public Period(Open open) {
        this.open = open;
    }


    public Open getOpen() {
        return open;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(open, period.open);
    }

    @Override
    public int hashCode() {
        return Objects.hash(open);
    }

    @Override
    public String toString() {
        return "Period{" +
                "open=" + open +
                '}';
    }

}
