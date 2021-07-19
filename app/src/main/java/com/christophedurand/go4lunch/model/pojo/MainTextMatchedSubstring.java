package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class MainTextMatchedSubstring {

    @SerializedName("length")
    private Integer length;
    @SerializedName("offset")
    private Integer offset;


    public MainTextMatchedSubstring(Integer length, Integer offset) {
        this.length = length;
        this.offset = offset;
    }


    public Integer getLength() {
        return length;
    }

    public Integer getOffset() {
        return offset;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainTextMatchedSubstring that = (MainTextMatchedSubstring) o;
        return Objects.equals(length, that.length) &&
                Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, offset);
    }

    @Override
    public String toString() {
        return "MainTextMatchedSubstring{" +
                "length=" + length +
                ", offset=" + offset +
                '}';
    }

}