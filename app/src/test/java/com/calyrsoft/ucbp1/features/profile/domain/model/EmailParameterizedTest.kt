package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert
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
        @Parameterized.Parameters(name = "Email.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("  USERDOMAIN@gmail.COM  ", "userdomain@gmail.com"),
            arrayOf("USER@gmail.COM", "user@gmail.com"),
            arrayOf("Foo.Bar@gmail.com", "foo.bar@gmail.com")
        )
    }

    @Test
    fun `test input Data Email`() {
        // act
        val emailValueObject = Email.Companion.create(inputData)

        // assert
        Assert.assertEquals(expected, emailValueObject.value)
    }
}