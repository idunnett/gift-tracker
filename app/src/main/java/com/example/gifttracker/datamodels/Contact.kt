package com.example.gifttracker.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String? = null,
    val hobbies: String? = null,
    )
