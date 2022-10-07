package com.example.howsMyStylist.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity (tableName = "STYLIST_TABLE")
public class Stylist {

    @PrimaryKey(autoGenerate = true)
    private long stylistId;
    @NonNull
    private String name;
    @Nullable
    private long phone;
    @Nullable
    private String profilePic;
    @Nullable
    private String email;
    @Nullable
    private String style;
    // for relationship
    @Nullable
    private long salonWorkingId;

    public Stylist (long stylistId, String name, long phone, String profilePic, String email
            , String style, long salonWorkingId){
        this.stylistId = stylistId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.profilePic = profilePic;
        this.style = style;
        this.salonWorkingId = salonWorkingId;
    }

    public long getStylistId() {
        return stylistId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public long getPhone() {
        return phone;
    }

    @Nullable
    public String getProfilePic() {
        return profilePic;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getStyle() {
        return style;
    }

    @Nullable

    public long getSalonWorkingId() {
        return salonWorkingId;
    }
}
