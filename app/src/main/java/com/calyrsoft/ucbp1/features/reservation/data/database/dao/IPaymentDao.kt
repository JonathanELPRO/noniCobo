package com.calyrsoft.ucbp1.features.reservation.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.PaymentEntity


@Dao
interface IPaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentEntity): Long


    @Query("SELECT SUM(amount) FROM payments WHERE reservationId = :reservationId")
    suspend fun sumByReservation(reservationId: Long): Double?
}