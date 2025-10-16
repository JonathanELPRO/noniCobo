package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCurrentUserUseCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = GetCurrentUserUseCase(repository)

    @Test
    fun `should return success when repository finds user by id`() = runBlocking {
        val expected = User.create(1L, "javi", "javi@example.com", null, "CLIENT")
        coEvery { repository.getById(1L) } returns Result.success(expected)

        val result = useCase(1L)

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure when repository fails`() = runBlocking {
        coEvery { repository.getById(2L) } returns Result.failure(Exception("No encontrado"))

        val result = useCase(2L)

        assertTrue(result.isFailure)
        assertEquals("No encontrado", result.exceptionOrNull()?.message)
    }
}