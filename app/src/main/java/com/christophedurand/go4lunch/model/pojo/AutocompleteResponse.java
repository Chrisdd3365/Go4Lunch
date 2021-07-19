package com.christophedurand.go4lunch.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


public class AutocompleteResponse {

    @SerializedName("predictions")
    private List<Prediction> predictions = null;
    @SerializedName("status")
    private String status;


    public AutocompleteResponse(List<Prediction> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }


    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutocompleteResponse that = (AutocompleteResponse) o;
        return Objects.equals(predictions, that.predictions) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predictions, status);
    }

    @Override
    public String toString() {
        return "AutocompleteResponse{" +
                "predictions=" + predictions +
                ", status='" + status + '\'' +
                '}';
    }

}