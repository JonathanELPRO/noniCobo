package com.calyrsoft.ucbp1.domain.repository

import com.calyrsoft.ucbp1.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow

interface IDollarRepository {
    fun getDollarFromFireBaseInMyLocalDB(): Flow<DollarModel>
    suspend fun getHistoryOfDollarsFromMyLocalDB(): Result<List<DollarModel>>
    suspend fun deleteByTimestamp(timestamp: Long): Result<Unit>

}
