package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterUserUseCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = RegisterUserUseCase(repository)

    @Test
    fun `should return success when local register ok (returns id)`() = runBlocking {
        val user = User.create(null, "javi", "javi@example.com", null, "USER")
        coEvery { repository.register(user, "123456") } returns Result.success(42L)

        val result = useCase(user, "123456")

        assertTrue(result.isSuccess)
        assertEquals(42L, result.getOrNull())
    }

    @Test
    fun `should return failure when local register fails`() = runBlocking {
        val user = User.create(null, "javi", "javi@example.com", null, "USER")
        coEvery { repository.register(user, any()) } returns Result.failure(Exception("Duplicado"))

        val result = useCase(user, "123")

        assertTrue(result.isFailure)
        assertEquals("Duplicado", result.exceptionOrNull()?.message)
    }
}