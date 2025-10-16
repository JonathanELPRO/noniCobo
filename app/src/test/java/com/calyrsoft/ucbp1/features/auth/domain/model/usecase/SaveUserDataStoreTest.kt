package com.calyrsoft.ucbp1.features.auth.domain.model.usecase

import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SaveUserDataStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveUserDataStoreTest {

    private val dsRepo = mockk<IAuthDataStoreRepository>(relaxed = true)
    private val useCase = SaveUserDataStore(dsRepo)

    @Test
    fun `should call repository to save user data`() = runBlocking {
        // arrange
        coEvery { dsRepo.saveUser("javi@example.com", "token123", "CLIENT") } returns Unit

        // act
        useCase.invoke("javi@example.com", "token123", "CLIENT")

        // assert (mismo estilo simple; opcionalmente verifica la llamada)
        coVerify(exactly = 1) {
            dsRepo.saveUser("javi@example.com", "token123", "CLIENT")
        }
        assertTrue(true) // mantiene el patrón de asserts simples
    }
}