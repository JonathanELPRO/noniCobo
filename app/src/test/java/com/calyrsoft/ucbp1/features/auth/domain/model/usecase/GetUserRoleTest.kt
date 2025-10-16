package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetUserRole
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUserRoleTest {

    private val dsRepo = mockk<IAuthDataStoreRepository>()
    private val useCase = GetUserRole(dsRepo)

    @Test
    fun `should return success role`() = runBlocking {
        coEvery { dsRepo.getUserRole() } returns Result.success("CLIENT")

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        assertEquals("CLIENT", result.getOrNull())
    }

    @Test
    fun `should return failure when datastore fails`() = runBlocking {
        coEvery { dsRepo.getUserRole() } returns Result.failure(Exception("No role"))

        val result = useCase.invoke()

        assertTrue(result.isFailure)
        assertEquals("No role", result.exceptionOrNull()?.message)
    }
}