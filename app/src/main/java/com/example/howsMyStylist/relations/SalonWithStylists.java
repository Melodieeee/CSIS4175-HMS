package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Salon;
import com.example.howsMyStylist.entities.Stylist;

import java.util.List;

public class SalonWithStylists {
    @Embedded
    private Salon salon;
    @Relation(
            parentColumn = "salonId",
            entityColumn = "salonWorkingId"
    )
    public List<Stylist> stylists;
}
