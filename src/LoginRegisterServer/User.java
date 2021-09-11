package LoginRegisterServer;

import java.io.Serializable;

public class User implements Serializable {
  private String firstName, lastName, icNumber, username, password;
  private boolean loggedIn;
  private int role;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  // For Returned Auth User
  public User(String firstName, String lastName, String icNumber, String username, boolean loggedIn, int role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.icNumber = icNumber;
    this.username = username;
    this.loggedIn = loggedIn;
	this.role = role;
  }

  // For New User
  public User(String firstName, String lastName, String icNumber, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.icNumber = icNumber;
    this.username = username;
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public String getIcNumber() {
    return icNumber;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
  
  public boolean getLoggedIn() {
      return loggedIn;
  }
  
  public int getRole(){
	return role;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setIcNumber(String icNumber) {
    this.icNumber = icNumber;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }
  
  public void setRole(int role) {
	this.role = role;
  }
}
