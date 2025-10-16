package com.calyrsoft.ucbp1.features.auth.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class UsernameParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Username.create({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("  javi  ", "javi"),
            arrayOf("User_123", "User_123"),
            arrayOf("user.name-01", "user.name-01")
        )
    }

    @Test
    fun `test input Data Username`() {
        val vo = Username.create(inputData)
        assertEquals(expected, vo.value)
    }
}