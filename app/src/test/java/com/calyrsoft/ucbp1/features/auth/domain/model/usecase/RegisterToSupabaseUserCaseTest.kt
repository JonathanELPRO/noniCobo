package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterToSupabaseUserCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterToSupabaseUserCaseTest {

    private val repository = mockk<IAuthRepository>()
    private val useCase = RegisterToSupabaseUserCase(repository)

    @Test
    fun `should return success when supabase register ok`() = runBlocking {
        val user = User.create(null, "javi", "javi@example.com", null, "CLIENT")
        val returned = User.create(5L, "javi", "javi@example.com", null, "CLIENT")
        coEvery { repository.registerToSupabase(user, "123456") } returns Result.success(returned)

        val result = useCase(user, "123456")

        assertTrue(result.isSuccess)
        assertEquals(returned, result.getOrNull())
    }

    @Test
    fun `should return failure when supabase register fails`() = runBlocking {
        val user = User.create(null, "javi", "javi@example.com", null, "CLIENT")
        coEvery { repository.registerToSupabase(user, any()) } returns Result.failure(Exception("Registro inválido"))

        val result = useCase(user, "123")

        assertTrue(result.isFailure)
        assertEquals("Registro inválido", result.exceptionOrNull()?.message)
    }
}