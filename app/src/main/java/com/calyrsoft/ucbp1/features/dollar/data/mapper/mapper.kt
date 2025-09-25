package com.calyrsoft.ucbp1.features.dollar.data.mapper

import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel

fun DollarEntity.toModel() : DollarModel {
    return DollarModel(
        dollarOfficial = dollarOfficial,
        dollarParallel = dollarParallel,
        timestamp = timestamp,
        usdt = usdt,
        usdc = usdc
    )
}


fun DollarModel.toEntity() : DollarEntity {
    return DollarEntity(
        dollarOfficial = dollarOfficial,
        dollarParallel = dollarParallel,
        timestamp = timestamp,
        usdt = usdt,
        usdc = usdc
    )
}
