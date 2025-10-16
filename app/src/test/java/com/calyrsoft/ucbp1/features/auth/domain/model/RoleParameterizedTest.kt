package com.calyrsoft.ucbp1.features.auth.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class RoleParameterizedTest(
    private val inputData: String?,
    private val expected: Role
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Role.from({0}) = {1}")
        fun data(): Collection<Array<out Any?>> = listOf(
            arrayOf("admin", Role.ADMIN),
            arrayOf("  ADMIN ", Role.ADMIN),
            arrayOf("client", Role.CLIENT),   // asegúrate de tener CLIENT en el enum
            arrayOf("user", Role.CLIENT),
            arrayOf(null, Role.CLIENT),
            arrayOf("unknown", Role.CLIENT)
        )
    }

    @Test
    fun `test input Data Role`() {
        val role = Role.from(inputData)
        assertEquals(expected, role)
    }
}