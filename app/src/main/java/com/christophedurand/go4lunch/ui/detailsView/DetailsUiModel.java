package com.christophedurand.go4lunch.ui.detailsView;


import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;


class DetailsUiModel {

    private final RestaurantDetails restaurantDetails;


    public DetailsUiModel(RestaurantDetails restaurantDetails) {
        this.restaurantDetails = restaurantDetails;
    }

    public RestaurantDetails getRestaurantDetails() {
        return restaurantDetails;
    }

}
