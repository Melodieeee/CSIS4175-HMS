package com.example.howsMyStylist.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PHOTO_TABLE")
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private long photoId;

    //for relationship
    private long ownerId; //salonId or reviewId or feedbackId

    private String photoAddress;

    public Photo(long photoId, String photoAddress, long ownerId){
        this.photoId = photoId;
        this.photoAddress = photoAddress;
        this.ownerId = ownerId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public String getPhotoAddress() {
        return photoAddress;
    }

    public long getOwnerId() { return ownerId; }
}
