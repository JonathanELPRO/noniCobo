package com.calyrsoft.ucbp1.features.auth.data.mapper

import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User

fun UserEntity.toDomain(): User =
    User.create(
        id = this.id,
        username = this.username,
        email = this.email,
        phone = this.phone,
        role = this.role
    )

fun User.toEntity(hash: String): UserEntity =
    UserEntity(
        id = this.id, // Room autogenera si viene null
        username = this.username.toString(),
        email = this.email.toString(),
        phone = this.phone?.toString(),
        role = this.role.name,
        passwordHash = hash
    )
