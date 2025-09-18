package com.calyrsoft.ucbp1.data.mapper

import com.calyrsoft.ucbp1.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.domain.model.DollarModel

fun DollarEntity.toModel() : DollarModel {
    return DollarModel(
        dollarOfficial = dollarOfficial,
        dollarParallel = dollarParallel,
        timestamp = timestamp
    )
}


fun DollarModel.toEntity() : DollarEntity {
    return DollarEntity(
        dollarOfficial = dollarOfficial,
        dollarParallel = dollarParallel,
        timestamp = timestamp)
}
