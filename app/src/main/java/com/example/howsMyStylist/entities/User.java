package com.example.howsMyStylist.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity (tableName = "USER_TABLE")
public class User {


    @PrimaryKey(autoGenerate = true)
    private long userId;

    private String firstName;
    private String lastName;
    private long phone;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    @NonNull
    private String email;
    private Date birthday;
    private String profileIntro;
    private String profilePic;

    // Address
    @ColumnInfo(name = "COUNTRY", defaultValue = "Canada")
    private String country;
    @ColumnInfo(name = "PROVINCE")
    private String province;
    @ColumnInfo(name = "CITY")
    private String city;
    @ColumnInfo(name = "STREET")
    private String street;
    @ColumnInfo(name = "POSTCODE")
    private String postCode;
    @ColumnInfo(name = "UNIT")
    @Nullable
    private String unit;


    private int verificationStatus;
    //    Deactivate = 0
    //    Active = 1
    //    Suspend = 2


    public User(long userId, String firstName, String lastName, long phone, String userName,
                String password, String email, String profileIntro, String profilePic, Date birthday,
                String country, String province, String city, String street,
                String postCode, String unit, int verificationStatus){

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.profileIntro = profileIntro;
        this.profilePic = profilePic;
        this.birthday = birthday;
        this.country = country;
        this.province = province;
        this.city = city;
        this.street = street;
        this.postCode = postCode;
        this.unit = unit;
        this.verificationStatus = verificationStatus;
    }

    public long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getPhone() {
        return phone;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getProfileIntro() {
        return profileIntro;
    }

    public String getProfilePic() {
        return profilePic;
    }

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

    @Nullable
    public String getUnit() {
        return unit;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }
}
