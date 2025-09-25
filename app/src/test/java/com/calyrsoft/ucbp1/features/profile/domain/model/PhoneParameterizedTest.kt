package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class PhoneParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Phone.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf(" 12345678 ", "12345678"),
            arrayOf("987654321", "987654321"),
            arrayOf("  7777777  ", "7777777")
        )
    }

    @Test
    fun `test input Data Phone`() {
        val vo = Phone.create(inputData)
        assertEquals(expected, vo.value)
    }
}
