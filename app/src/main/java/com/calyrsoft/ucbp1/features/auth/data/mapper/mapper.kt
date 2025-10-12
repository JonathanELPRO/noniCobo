package com.calyrsoft.ucbp1.features.auth.data.mapper

import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        phone = phone,
        role = Role.valueOf(role)
    )
}

fun User.toEntity(hash: String): UserEntity {
    return UserEntity(
        id = id ?: 0,
        username = username,
        email = email,
        phone = phone,
        role = role.name,
        passwordHash = hash
    )
}
