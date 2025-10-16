package com.calyrsoft.ucbp1.features.lodging.data.datasource



import com.calyrsoft.ucbp1.features.lodging.domain.model.AddModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class RealTimeRemoteDataSource2 {


    fun getAddsUpdate(): Flow<AddModel> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                close(p0.toException())
            }
            override fun onDataChange(p0: DataSnapshot) {
//                val value = p0.getValue(String::class.java)eastern.eel.wegs@hidesmail.net
                val value = p0.getValue(AddModel::class.java)
                if (value != null) {
                    val withTimestamp = value.copy(
                        timestamp = System.currentTimeMillis()
                    )
                    trySend(withTimestamp)
                    //lo de arriba vuelve el Model a un Flow para que mi view model y screen lo consuman en tiempo real
                }
            }
        }


//         Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("dollar")
        myRef.addValueEventListener(callback)


        awaitClose {
            myRef.removeEventListener(callback)
        }
    }

    //callBackFlows convierte un conjunto de callbacks (listener) en flow los cuales vamos a poder coleccionar
    //asi que vamos a poder coleccionar o escuchar en tiempo real los cambios que lllegue a getDollarUpdates
    //En el callBackFlows estamos definiendo que nuestra referencia a firebase osea:
    //:val database = Firebase.database
    //val myRef = database.getReference("dollar")
    //debe conocer un listener asi como tambien debe saber como limpiarlo cuando ya no lo usemos
    //Flow es la herramienta para: “avísame cada vez que algo cambie y dame el valor actualizado”.
    //val callback = object : ValueEventListener {
    //la linea de arriba en realidad deberia llamarse listener
    //ya que esta implementando una interfaz llamada ValueEventListener
    //la cual es un conjunto de callbacks, y al conjunto de callbacks lo llamamos listener
    //normlamente lo que haciamos era crear una clase que implementa esa interfaz
    // a dicha clase la podiamos llamar MyListener() y luego crear una variable asi:
    //val callback = MyListener()
    //pero en lugar de todo eso es mejor crear una variable en este caso el
    //lo llamaa callback y decimos que ese callback sera igual a
    //crear un objeto anónimo (una clase sin nombre) que implemente la interfaz con los metodos que yo le dire
    // .

}