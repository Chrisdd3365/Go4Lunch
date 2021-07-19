package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


public class Prediction {

    @SerializedName("description")
    private String description;
    @SerializedName("matched_substrings")
    private List<MatchedSubstring> matchedSubstrings = null;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("reference")
    private String reference;
    @SerializedName("structured_formatting")
    private StructuredFormatting structuredFormatting;
    @SerializedName("terms")
    private List<Term> terms = null;
    @SerializedName("types")
    private List<String> types = null;


    public Prediction(String description,
                      List<MatchedSubstring> matchedSubstrings,
                      String placeId,
                      String reference,
                      StructuredFormatting structuredFormatting,
                      List<Term> terms,
                      List<String> types) {
        this.description = description;
        this.matchedSubstrings = matchedSubstrings;
        this.placeId = placeId;
        this.reference = reference;
        this.structuredFormatting = structuredFormatting;
        this.terms = terms;
        this.types = types;
    }


    public String getDescription() {
        return description;
    }

    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getReference() {
        return reference;
    }

    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public List<String> getTypes() {
        return types;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prediction that = (Prediction) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(matchedSubstrings, that.matchedSubstrings) &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(reference, that.reference) &&
                Objects.equals(structuredFormatting, that.structuredFormatting) &&
                Objects.equals(terms, that.terms) &&
                Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchedSubstrings, placeId, reference, structuredFormatting, terms, types);
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "description='" + description + '\'' +
                ", matchedSubstrings=" + matchedSubstrings +
                ", placeId='" + placeId + '\'' +
                ", reference='" + reference + '\'' +
                ", structuredFormatting=" + structuredFormatting +
                ", terms=" + terms +
                ", types=" + types +
                '}';
    }

}
