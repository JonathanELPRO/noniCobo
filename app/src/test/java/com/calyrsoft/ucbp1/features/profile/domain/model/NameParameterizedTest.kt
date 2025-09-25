package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class NameParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Name.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("  Calyr  ", "calyr"),
            arrayOf("admin", "admin"),
            arrayOf(" JONATHAN ", "jonathan")
        )
    }

    @Test
    fun `test input Data Name`() {
        val vo = Name.create(inputData)
        assertEquals(expected, vo.value)
    }
}
