package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class ImageUrlParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "ImageUrl.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf(" HTTPS://EXAMPLE.com/IMAGE.png ", "https://example.com/image.png"),
            arrayOf("httpS://test.com/photo.jpg", "https://test.com/photo.jpg"),
            arrayOf("  https://github.com/avatar.png  ", "https://github.com/avatar.png")
        )
    }

    @Test
    fun `test input Data ImageUrl`() {
        val vo = ImageUrl.create(inputData)
        assertEquals(expected, vo.value)
    }
}
