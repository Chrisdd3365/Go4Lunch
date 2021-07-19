package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


public class StructuredFormatting {

    @SerializedName("main_text")
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    private List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
    @SerializedName("secondary_text")
    private String secondaryText;


    public StructuredFormatting(String mainText,
                                List<MainTextMatchedSubstring> mainTextMatchedSubstrings,
                                String secondaryText) {
        this.mainText = mainText;
        this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
        this.secondaryText = secondaryText;
    }


    public String getMainText() {
        return mainText;
    }

    public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstrings;
    }

    public String getSecondaryText() {
        return secondaryText;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuredFormatting that = (StructuredFormatting) o;
        return Objects.equals(mainText, that.mainText) &&
                Objects.equals(mainTextMatchedSubstrings, that.mainTextMatchedSubstrings) &&
                Objects.equals(secondaryText, that.secondaryText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainText, mainTextMatchedSubstrings, secondaryText);
    }

    @Override
    public String toString() {
        return "StructuredFormatting{" +
                "mainText='" + mainText + '\'' +
                ", mainTextMatchedSubstrings=" + mainTextMatchedSubstrings +
                ", secondaryText='" + secondaryText + '\'' +
                '}';
    }

}
