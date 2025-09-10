package com.calyrsoft.ucbp1.domain.error
sealed class Failure : Throwable() {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object NotFound : Failure()
    object EmptyBody : Failure()
    data class Unknown(val exception: Throwable) : Failure()
}


/*sealed class DataException : Exception() → son errores técnicos (API, red, parsing).

sealed class Failure : Throwable() → son errores de dominio, lo que tu app realmente entiende y sabe mostrar a usuario.*/
//una sealed class sirve cuando quieres representar estados y usar el when
//un object nunca va a cambair es como una ctte en realidad es un singleton
// en cambio una class puede tener varias instancias y puede tener atributos que cambian