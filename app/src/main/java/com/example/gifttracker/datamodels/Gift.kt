package com.example.gifttracker.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Gift(
    @PrimaryKey(autoGenerate = true) val giftId : Int = 0,
    val name : String,
    val eventId : Int,
    val imageUri: String? = null,
    val link: String? = null
)
