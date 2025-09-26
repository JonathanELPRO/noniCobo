package com.calyrsoft.ucbp1.features.movie.data.mapper

import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.movie.data.database.entity.MovieEntity
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel

fun MovieEntity.toModel() : MovieModel {
    return MovieModel(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}


fun MovieModel.toEntity() : MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}
