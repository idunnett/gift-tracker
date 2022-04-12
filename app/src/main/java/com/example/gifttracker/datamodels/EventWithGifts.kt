package com.example.gifttracker.datamodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class EventWithGifts(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    ) val gifts: List<Gift> = emptyList()
)
