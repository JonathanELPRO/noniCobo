package com.calyrsoft.ucbp1.features.github.domain.model

import com.calyrsoft.ucbp1.domain.model.UrlPath
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class UrlPathParameterizedTest(
    private val inputData: String,
    private val expected: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "UrlPath.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf(" https://EXAMPLE.com ", "https://example.com"), // normaliza con trim
            arrayOf("  https://github.com  ", "https://github.com"),
            arrayOf("  https://test.url/page  ", "https://test.url/page")
        )
    }

    @Test
    fun `test valid UrlPath`() {
        // Act
        val urlPath = UrlPath.Companion.create(inputData)

        // Assert
        assertEquals(expected, urlPath.value)
    }
}
