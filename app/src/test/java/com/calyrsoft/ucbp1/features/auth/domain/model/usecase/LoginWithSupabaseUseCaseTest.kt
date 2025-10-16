package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginWithSupabaseUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginWithSupabaseUseCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = LoginWithSupabaseUseCase(repository)

    @Test
    fun `should return success when supabase login ok`() = runBlocking {
        val expected = User.create(10L, "calyr", "calyr@gmail.com", "+59170000000", "CLIENT")
        coEvery { repository.loginWithSupabase("calyr@gmail.com", "123456") } returns Result.success(expected)

        val result = useCase("calyr@gmail.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure when supabase login fails`() = runBlocking {
        coEvery { repository.loginWithSupabase(any(), any()) } returns Result.failure(Exception("Credenciales Invalidos."))

        val result = useCase("x@y.com", "bad")

        assertTrue(result.isFailure)
        assertEquals("Credenciales Invalidos.", result.exceptionOrNull()?.message)
    }
}