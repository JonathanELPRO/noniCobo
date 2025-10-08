package com.calyrsoft.ucbp1.features.auth.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity


@Dao
interface IUserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long


    @Query("SELECT * FROM users WHERE (username = :userOrEmail OR email = :userOrEmail) AND passwordHash = :hash LIMIT 1")
    suspend fun login(userOrEmail: String, hash: String): UserEntity?


    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun findById(id: Long): UserEntity?
}