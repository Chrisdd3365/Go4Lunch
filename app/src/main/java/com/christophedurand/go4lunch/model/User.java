package com.christophedurand.go4lunch.model;


import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

   private String name;
   private String emailAddress;
   private String avatarURL;


   public User(String name, String emailAddress, String avatarURL) {
      this.name = name;
      this.emailAddress = emailAddress;
      this.avatarURL = avatarURL;
   }


   public String getName() {
      return name;
   }

   public String getEmailAddress() {
      return emailAddress;
   }

   public String getAvatarURL() {
      return avatarURL;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return Objects.equals(name, user.name) && Objects.equals(emailAddress, user.emailAddress) && Objects.equals(avatarURL, user.avatarURL);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, emailAddress, avatarURL);
   }

   @Override
   public String toString() {
      return "User{" +
              "name='" + name + '\'' +
              ", emailAddress='" + emailAddress + '\'' +
              ", avatarURL='" + avatarURL + '\'' +
              '}';
   }

}
