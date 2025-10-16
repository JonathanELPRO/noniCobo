package com.calyrsoft.ucbp1.features.auth.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class UserCreateTest {

    @Test
    fun `should create User successfully`() {
        // Arrange
        val username = "Calyr"
        val email = "calyr@gmail.com"
        val phone = "70000000"
        val role = "ADMIN"

        // Act
        val user = User.create(
            id = 1L,
            username = username,
            email = email,
            phone = phone,
            role = role
        )

        // Assert (mismo estilo que LoginUserModelTest)
        assertEquals(1L, user.id)
        assertEquals("calyr", user.username.value)
        assertEquals(email, user.email.value)
        assertEquals(phone, user.phone?.value)
        assertEquals(role, user.role.name)
    }

    @Test
    fun `should throw exception when email is invalid`() {
        assertThrows(IllegalArgumentException::class.java) {
            User.create(
                id = null,
                username = "Calyr",
                email = "email invalido",
                phone = "70000000",
                role = "USER"
            )
        }
    }
}