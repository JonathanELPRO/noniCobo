package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `should return success when login is correct`() = runBlocking {
        val expected = User.create(1L, "javi", "javi@example.com", null, "ADMIN")
        coEvery { repository.login("javi", "123456") } returns Result.success(expected)

        val result = useCase("javi", "123456")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure on wrong credentials`() = runBlocking {
        coEvery { repository.login("javi", "bad") } returns Result.failure(Exception("Usuario o contraseña incorrectos"))

        val result = useCase("javi", "bad")

        assertTrue(result.isFailure)
        assertEquals("Usuario o contraseña incorrectos", result.exceptionOrNull()?.message)
    }
}