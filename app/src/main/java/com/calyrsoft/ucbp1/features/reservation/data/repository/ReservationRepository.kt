package com.calyrsoft.ucbp1.features.reservation.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.calyrsoft.ucbp1.features.reservation.data.datasource.ReservationLocalDataSource
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.PaymentEntity
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.ReservationEntity
import com.calyrsoft.ucbp1.features.reservation.domain.model.Payment
import com.calyrsoft.ucbp1.features.reservation.domain.model.PaymentType
import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import com.calyrsoft.ucbp1.features.reservation.domain.model.StayType
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IReservationRepository


class ReservationRepository(private val ds: ReservationLocalDataSource) : IReservationRepository {
    override suspend fun upsert(reservation: Reservation): Long =
        ds.upsertReservation(reservation.toEntity())


    override fun observeByUser(userId: Long): Flow<List<Reservation>> =
        ds.observeReservationsByUser(userId).map { list -> list.map { it.toDomain() } }


    private fun Reservation.toEntity() = ReservationEntity(
        id = id ?: 0,
        userId = userId,
        lodgingId = lodgingId,
        stayType = stayType.name,
        hours = hours,
        startMillis = startMillis,
        endMillis = endMillis,
        status = status,
        total = total,
        advancePaid = advancePaid
    )


    private fun ReservationEntity.toDomain() = Reservation(
        id = id,
        userId = userId,
        lodgingId = lodgingId,
        stayType = StayType.valueOf(stayType),
        hours = hours,
        startMillis = startMillis,
        endMillis = endMillis,
        status = status,
        total = total,
        advancePaid = advancePaid
    )
}