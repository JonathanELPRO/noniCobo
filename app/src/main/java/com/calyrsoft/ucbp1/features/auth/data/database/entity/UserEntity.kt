package com.calyrsoft.ucbp1.features.auth.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val email: String,
    val phone: String?,
    val passwordHash: String,
    val role: String
)


