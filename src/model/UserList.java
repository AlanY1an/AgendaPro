package model;

import java.util.ArrayList;

public class UserList {
      ArrayList<User> userList;
      
      
      UserList(){
    	  userList= new ArrayList<>();
      }
      
      
      public User searchUser(String username) {
          for (User user : userList) {
              if (user.getUsername().equalsIgnoreCase(username)) {
                  return user;
              }
          }
          return null; // User not found
      }
      
      public void addNewUser(String name, String password) {
          if (searchUser(name) != null) {
              throw new IllegalArgumentException("Username is already taken.");
          }
          User newUser = new User(name, password);
          userList.add(newUser);
      }
      
      public boolean removeUser(String username) {
          User user = searchUser(username);
          if (user != null) {
              userList.remove(user);
              return true;
          }
          return false; // User not found
      }
      
      public ArrayList<User> listUsers() {
          return userList;
      }
      
}
