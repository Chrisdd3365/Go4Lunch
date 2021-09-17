package com.christophedurand.go4lunch.model;


import java.util.Objects;


public class Restaurant {

   private String id = "";
   private String name = "";


   public Restaurant(String id, String name) {
      this.id = id;
      this.name = name;
   }

   public Restaurant() { }


   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Restaurant that = (Restaurant) o;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name);
   }

   @Override
   public String toString() {
      return "Restaurant{" +
              "id='" + id + '\'' +
              ", name='" + name + '\'' +
              '}';
   }

}
