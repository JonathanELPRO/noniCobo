package com.calyrsoft.ucbp1.features.auth.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class EmailParameterizedTest(
    private val inputData: String,
    private val expected: String,
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Email.create({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("  USERDOMAIN@gmail.COM  ", "userdomain@gmail.com"),
            arrayOf("USER@gmail.COM", "user@gmail.com"),
            arrayOf("Foo.Bar@gmail.com", "foo.bar@gmail.com")
        )
    }

    @Test
    fun `test input Data Email`() {
        val emailValueObject = Email.create(inputData)
        assertEquals(expected, emailValueObject.value)
    }
}