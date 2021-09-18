package com.christophedurand.go4lunch.model;


import java.util.Objects;


public class Restaurant {

   private String id = "";
   private String name = "";
   private String address = "";


   public Restaurant(String id, String name, String address) {
      this.id = id;
      this.name = name;
      this.address = address;
   }

   public Restaurant() { }


   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getAddress() {
      return address;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Restaurant that = (Restaurant) o;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, address);
   }

   @Override
   public String toString() {
      return "Restaurant{" +
              "id='" + id + '\'' +
              ", name='" + name + '\'' +
              ", address='" + address + '\'' +
              '}';
   }

}
