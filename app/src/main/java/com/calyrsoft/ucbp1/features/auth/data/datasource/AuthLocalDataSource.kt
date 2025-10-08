package com.calyrsoft.ucbp1.features.auth.data.datasource

import com.calyrsoft.ucbp1.features.auth.data.database.dao.IUserDao
import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity


class AuthLocalDataSource(private val userDao: IUserDao) {
    suspend fun register(user: UserEntity) = userDao.insert(user)
    suspend fun login(userOrEmail: String, hash: String) = userDao.login(userOrEmail, hash)
    suspend fun findById(id: Long) = userDao.findById(id)
}