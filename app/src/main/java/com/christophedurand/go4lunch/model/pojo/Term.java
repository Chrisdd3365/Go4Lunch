package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class Term {

    @SerializedName("offset")
    private Integer offset;
    @SerializedName("value")
    private String value;


    public Term(Integer offset, String value) {
        this.offset = offset;
        this.value = value;
    }


    public Integer getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(offset, term.offset) &&
                Objects.equals(value, term.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, value);
    }


    @Override
    public String toString() {
        return "Term{" +
                "offset=" + offset +
                ", value='" + value + '\'' +
                '}';
    }

}
