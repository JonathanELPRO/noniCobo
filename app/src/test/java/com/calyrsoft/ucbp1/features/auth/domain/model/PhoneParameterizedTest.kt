package com.calyrsoft.ucbp1.features.auth.domain.model

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
        @Parameterized.Parameters(name = "Phone.create({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf(" +59170000000 ", "+59170000000"),
            arrayOf("(591) 700-00000", "(591) 700-00000"),
            arrayOf(" 700 00 000 ", "700 00 000")
        )
    }

    @Test
    fun `test input Data Phone`() {
        val vo = Phone.create(inputData)
        assertEquals(expected, vo.value)
    }
}