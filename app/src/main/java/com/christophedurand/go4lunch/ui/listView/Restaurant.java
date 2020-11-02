package com.christophedurand.go4lunch.ui.listView.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class RestaurantContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Restaurant> RESTAURANTS = new ArrayList<>();

//    /**
//     * A map of sample (dummy) items, by ID.
//     */
//    public static final Map<String, Restaurant> RESTAURANT_ITEM_MAP = new HashMap<>();

//    private static final int COUNT = 25;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createRestaurantItem(i));
//        }
//    }

    private static void addItem(Restaurant restaurant) {
        RESTAURANTS.add(restaurant);
        //RESTAURANT_ITEM_MAP.put(restaurant.id, restaurant);
    }

//    private static Restaurant createRestaurantItem(int position) {
//        return new Restaurant(String.valueOf(position), "Restaurant " + position, makeDetails(position));
//    }

//    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Restaurant {
        public final String id;
        public final String name;
        public final String address;
        public final String businessStatus;

        public Restaurant(String id, String name, String address, String businessStatus) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.businessStatus = businessStatus;
        }

//        @Override
//        public String toString() {
//            return content;
//        }
    }
}