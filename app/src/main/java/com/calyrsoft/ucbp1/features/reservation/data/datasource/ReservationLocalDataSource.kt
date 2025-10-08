package com.calyrsoft.ucbp1.features.reservation.data.datasource

import com.calyrsoft.ucbp1.features.reservation.data.database.dao.IPaymentDao
import com.calyrsoft.ucbp1.features.reservation.data.database.dao.IReservationDao
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.PaymentEntity
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow



class ReservationLocalDataSource(
    private val reservationDao: IReservationDao,
    private val paymentDao: IPaymentDao
) {
    suspend fun upsertReservation(entity: ReservationEntity) = reservationDao.upsert(entity)
    fun observeReservationsByUser(userId: Long): Flow<List<ReservationEntity>> = reservationDao.observeByUser(userId)
    suspend fun insertPayment(payment: PaymentEntity) = paymentDao.insert(payment)
    suspend fun sumPayments(reservationId: Long) = paymentDao.sumByReservation(reservationId) ?: 0.0
}