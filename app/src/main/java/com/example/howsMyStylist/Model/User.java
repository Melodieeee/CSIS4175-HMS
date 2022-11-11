package com.example.howsMyStylist.Model;

import java.util.Date;

public class User {
    private String fname;
    private String lname;
    private String phone;
    private String username;
    private String password;
    private String email;
    private String birth;
    private String address;
    private String country;
    private String state;
    private String city;
    private String zip;
    private int vStatus;
    private String picture;
    private String intro;

    public User (){

    }

    public User (String editUsername, String editPassword){
        username = editUsername;
        password = editPassword;
    }

    public User(String username, String email, String phone, String pwd, String birthday, String firstname, String lastname) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = pwd;
        this.birth = birthday;
        this.fname = firstname;
        this.lname = lastname;
    }

    public User(String firstname, String lastname, String dob, String phone, String address, String city, String state, String zip, String country) {
        this.fname = firstname;
        this.lname = lastname;
        this.birth = dob;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getvStatus() {
        return vStatus;
    }

    public void setvStatus(int vStatus) {
        this.vStatus = vStatus;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
