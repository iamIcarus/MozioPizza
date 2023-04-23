package com.mozio.moziopizza.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hash")
data class HashEntity(
    @PrimaryKey
    val id: Int = 1,
    val dataHash: String,
)