package com.christophedurand.go4lunch.model;

import java.util.Objects;


public class Restaurant {

   private final String id;


   public Restaurant(String id) {
      this.id = id;
   }


   public String getId() {
      return id;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Restaurant that = (Restaurant) o;
      return Objects.equals(id, that.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "Restaurant{" +
              "id='" + id + '\'' +
              '}';
   }

}
