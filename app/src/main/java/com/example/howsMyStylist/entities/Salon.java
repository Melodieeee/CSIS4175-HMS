package com.example.howsMyStylist.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SALON_TABLE")
public class Salon {

    @PrimaryKey(autoGenerate = true)
    private long salonId;

    private String name;
    // Address
    @ColumnInfo(name="COUNTRY", defaultValue = "Canada")
    private String country;
    @ColumnInfo(name="PROVINCE")
    private String province;
    @ColumnInfo(name="CITY")
    private String city;
    @ColumnInfo(name="STREET")
    private String street;
    @ColumnInfo(name="POSTCODE")
    private String postCode;
    @ColumnInfo(name="UNIT")
    private String unit;

    private String profilePicture;
    private String link;
    private long phoneNumber;

//    @OneToMany
//    private List<Photo> photos;

    public Salon(long salonId, String name, String country, String province, String city, String street,
                 String postCode, String unit, String profilePicture, String link, long phoneNumber){
        this.salonId = salonId;
        this.country = country;
        this.province = province;
        this.city = city;
        this.street = street;
        this.postCode = postCode;
        this.unit = unit;
        this.profilePicture = profilePicture;
        this.link = link;
        this.phoneNumber = phoneNumber;
//        this.photos = photos;
    }

    public long getSalonId() {
        return salonId;
    }

    public String getName() {return name;}

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getUnit() {
        return unit;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLink() {
        return link;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

//    public List<Photo> getPhotos() {
//        return photos;
//    }
}
