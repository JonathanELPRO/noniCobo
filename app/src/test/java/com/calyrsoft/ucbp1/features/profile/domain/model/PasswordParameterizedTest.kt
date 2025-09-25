package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class PasswordParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Password.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("123456", "123456"),
            arrayOf("abcdef", "abcdef"),
            arrayOf(" nonisito123 ", "nonisito123")
        )
    }

    @Test
    fun `test input Data Password`() {
        val vo = Password.create(inputData)
        assertEquals(expected, vo.value)
    }
}
