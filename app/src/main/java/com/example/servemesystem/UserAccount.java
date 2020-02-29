package com.example.servemesystem;

public class UserAccount {

    public static String USERID = "UserId";
    public static String USERTYPE = "UserType";
    public static String USERNAME = "Username";

    int userId;
    String userType;
    String username;
    String email;
    String address;
    String firstName;
    String lastName;
    String phone;
    int points;
    double rating;

    public int getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public int getPoints() {
        return points;
    }

    public double getRating() {
        return rating;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
