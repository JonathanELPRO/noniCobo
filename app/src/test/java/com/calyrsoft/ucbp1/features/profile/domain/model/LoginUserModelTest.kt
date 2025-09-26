package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LoginUserModelTest {

    @Test
    fun `should create LoginUserModel successfully`() {
        // Arrange
        val name = "Calyr"
        val email = "calyr@gmail.com"
        val password = "123456"
        val phone = "77777777"
        val imageUrl = "https://github.com/avatar.png"
        val summary = "Usuario de prueba"

        // Act
        val user = LoginUserModel.create(
            name = name,
            email = email,
            password = password,
            phone = phone,
            imageUrl = imageUrl,
            summary = summary
        )

        // Assert
        assertEquals("calyr", user.name.value)
        assertEquals(email, user.email.value)
        assertEquals(password, user.password.value)
        assertEquals(phone, user.phone.value)
        assertEquals(imageUrl, user.imageUrl.value)
        assertEquals(summary, user.summary.value)
    }

    @Test
    fun `should throw exception when email is invalid`() {
        // Arrange
        val invalidEmail = "email invalido"

        //
        assertThrows(IllegalArgumentException::class.java) {
            LoginUserModel.create(
                name = "Calyr",
                email = invalidEmail,
                password = "123456",
                phone = "77777777",
                imageUrl = "https://github.com/avatar.png",
                summary = "Usuario de prueba"
            )
        }
    }

}
