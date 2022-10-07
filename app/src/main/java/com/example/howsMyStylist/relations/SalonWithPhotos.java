package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Photo;
import com.example.howsMyStylist.entities.Salon;

import java.util.List;

public class SalonWithPhotos {
    @Embedded private Salon salon;
    @Relation(
            parentColumn = "salonId",
            entityColumn = "ownerId"
    )
    public List<Photo> photos;

}
