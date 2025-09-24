package com.calyrsoft.ucbp1.features.github.domain.model.usecase

import androidx.annotation.OptIn
import com.calyrsoft.ucbp1.domain.model.Nickname
import com.calyrsoft.ucbp1.domain.model.UserModel
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.domain.usecase.FindByNickNameUseCase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.calyrsoft.ucbp1.domain.model.UrlPath


@OptIn(ExperimentalCoroutinesApi::class)
class FindByNickNameUseCaseTest {

    private val repository = mockk<IGithubRepository>()
    private val useCase = FindByNickNameUseCase(repository)

    @Test
    fun `should return success when repository returns user`() = runTest {
        // Arrange
        val expected = UserModel(nickname = Nickname("juan"), pathUrl = UrlPath(value = "https://test.url"))
        coEvery { repository.findByNick(value = "juan") } returns Result.success(value = expected)

        // Act
        val result = useCase.invoke(nickname = "juan")

        // Assert
        assert(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }


}
