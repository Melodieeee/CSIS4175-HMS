package com.example.howsMyStylist.Model;

public class Stylist {

    private String fName;
    private String lName;
    private String gender;
    private String salonName;
    private String uriStylistPic;
    private String phone;
    private double avgRating;
    private String email;

    public Stylist(String fName, String lName, String gender, String salonName) {
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.salonName = salonName;
    }

    public Stylist() {

    }

    public Stylist(String firstname, String lastname, String phone, String email, String gender, String salonName, String uriImage, double avgRating) {
        this.fName = firstname;
        this.lName = lastname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.salonName = salonName;
        this.uriStylistPic = uriImage;
        this.avgRating = avgRating;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String geturiStylistPic() {
        return uriStylistPic;
    }

    public void seturiStylistPic(String uriStylistPic) {
        this.uriStylistPic = uriStylistPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
