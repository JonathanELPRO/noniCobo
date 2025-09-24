package com.calyrsoft.ucbp1.features.github.domain.model

import com.calyrsoft.ucbp1.domain.model.Nickname
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class NicknameParameterizedTest(
    private val inputData: String,
    private val expected: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Nickname.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("User123", "User123"),
            arrayOf(" foo_bar ", "foo_bar"),
            arrayOf("NickName", "NickName")
        )
    }

    @Test
    fun `test valid Nickname`() {
        // act
        val nicknameValueObject = Nickname.Companion.create(inputData)

        // assert
        Assert.assertEquals(expected, nicknameValueObject.value)
        //arriba accedo a .value para acceder al dato crudo del Objeto de valor, si no hago eso
        //nicknameValueObject solo retorna el objeto de valor y expected son strings
        //recuerdas que hiciste un override de ToString y uno pensaria a ya entonces siempre deberia obtener el dato crudo
        //del objeti de valor y eso es mentira ese override sirve para obtener el dato crudo osea el .value
        //cuando haces algun print
    }
}