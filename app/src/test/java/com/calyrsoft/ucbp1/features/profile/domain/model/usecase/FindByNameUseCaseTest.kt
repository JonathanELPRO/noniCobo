package com.calyrsoft.ucbp1.features.profile.domain.model.usecase

import com.calyrsoft.ucbp1.features.profile.domain.model.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FindByNameUseCaseTest {

    private val repository = mockk<ILoginRepository>()
    private val useCase = FindByNameUseCase(repository)

    @Test
    fun `should return success when repository finds user`() {
        //arrange
        val expected = LoginUserModel.create(
            name = "calyr",
            email = "calyr@gmail.com",
            password = "123456",
            phone = "12345678",
            imageUrl = "https://avatars.githubusercontent.com/u/874321?v=4",
            summary = "Esta es la descripción de calyr"
        )
        every { repository.findByName("calyr") } returns Result.success(expected)

        //act
        val result = useCase("calyr")

        //assert
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure when repository does not find user`() {
        // Arrange
        every { repository.findByName("juanito") } returns Result.failure(Exception("Usuario o contraseña incorrectos"))

        // Act
        val result = useCase("juanito")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Usuario o contraseña incorrectos", result.exceptionOrNull()?.message)
    }
}
