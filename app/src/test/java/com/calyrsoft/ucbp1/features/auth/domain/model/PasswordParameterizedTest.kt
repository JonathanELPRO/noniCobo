package com.calyrsoft.ucbp1.features.auth.domain.model

import com.calyrsoft.ucbp1.features.profile.domain.model.Password
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
        @Parameterized.Parameters(name = "Password.create({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("abc12345", "abc12345"),      // min 8, letra + dígito
            arrayOf(" noni1234 ", " noni1234 "),  // se guarda tal cual (no trim forzado)
            arrayOf("Passw0rd", "Passw0rd")
        )
    }

    @Test
    fun `test input Data Password`() {
        val vo = Password.create(inputData)
        assertEquals(expected, vo.value)
    }
}