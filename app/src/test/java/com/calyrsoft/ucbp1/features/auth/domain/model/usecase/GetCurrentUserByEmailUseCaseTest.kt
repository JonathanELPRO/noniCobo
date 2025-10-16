package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserByEmailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCurrentUserByEmailUseCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = GetCurrentUserByEmailUseCase(repository)

    @Test
    fun `should return success when repository finds user by email`() = runBlocking {
        // arrange
        val expected = User.create(
            id = 1L,
            username = "javi",
            email = "javi@example.com",
            phone = "+59170000000",
            role = "ADMIN"
        )
        coEvery { repository.getByEmail("javi@example.com") } returns Result.success(expected)

        // act
        val result = useCase("javi@example.com")

        // assert
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure when repository does not find user by email`() = runBlocking {
        // arrange
        coEvery { repository.getByEmail("nope@example.com") } returns Result.failure(Exception("No encontrado"))

        // act
        val result = useCase("nope@example.com")

        // assert
        assertTrue(result.isFailure)
        assertEquals("No encontrado", result.exceptionOrNull()?.message)
    }
}