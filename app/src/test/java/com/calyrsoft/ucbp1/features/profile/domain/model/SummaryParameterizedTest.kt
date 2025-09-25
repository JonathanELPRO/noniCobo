package com.calyrsoft.ucbp1.features.profile.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class SummaryParameterizedTest(
    private val inputData: String,
    private val expected: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Summary.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf(" Descripción corta ", "Descripción corta"),
            arrayOf("   Hola mundo   ", "Hola mundo"),
            arrayOf("Resumen sin cambios", "Resumen sin cambios")
        )
    }

    @Test
    fun `test input Data Summary`() {
        val vo = Summary.create(inputData)
        assertEquals(expected, vo.value)
    }
}
