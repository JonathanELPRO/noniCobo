package com.calyrsoft.ucbp1.features.auth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.calyrsoft.ucbp1.features.auth.data.database.dao.IUserDao
import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity
import com.calyrsoft.ucbp1.features.lodging.data.database.dao.ILodgingDao
import com.calyrsoft.ucbp1.features.lodging.data.database.dao.IRoomTypeDao
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.RoomTypeEntity
import com.calyrsoft.ucbp1.features.reservation.data.database.dao.IPaymentDao
import com.calyrsoft.ucbp1.features.reservation.data.database.dao.IReservationDao
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.PaymentEntity
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.ReservationEntity


@Database(
    entities = [
        UserEntity::class,
        LodgingEntity::class,
        RoomTypeEntity::class,
        ReservationEntity::class,
        PaymentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun userDao(): IUserDao
    abstract fun lodgingDao(): ILodgingDao
    abstract fun roomTypeDao(): IRoomTypeDao
    abstract fun reservationDao(): IReservationDao
    abstract fun paymentDao(): IPaymentDao
}