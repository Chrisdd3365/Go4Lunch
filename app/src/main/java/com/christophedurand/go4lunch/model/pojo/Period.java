package com.christophedurand.go4lunch.model.pojo;


import com.google.gson.annotations.SerializedName;


public class Period {

    @SerializedName("open")
    private Open open;


    public Period(Open open) {
        this.open = open;
    }


    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }


    @Override
    public String toString() {
        return "Period{" +
                "open=" + open +
                '}';
    }

}
