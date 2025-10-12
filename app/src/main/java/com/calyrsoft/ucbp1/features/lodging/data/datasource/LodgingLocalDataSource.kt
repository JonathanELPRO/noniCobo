package com.calyrsoft.ucbp1.features.lodging.data.datasource

import com.calyrsoft.ucbp1.features.lodging.data.database.dao.ILodgingDao
import com.calyrsoft.ucbp1.features.lodging.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.lodging.data.mapper.toModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LodgingLocalDataSource(
    private val lodgingDao: ILodgingDao
) {
    fun observeAll(): Flow<List<Lodging>> {
        return lodgingDao.observeAll().map { listLodgingEntities -> listLodgingEntities.map { it.toModel() } }
        //nota que lodgingDao.observeAll() devuelve un flow de lista de algo
        //un flow puede emitir muchos valores a futuro es decir muchas listas de lodgings
        //por eso le aplicamos un map, es decir: map { list -> list.map { it.toModel() } }
        //osea a cada lista que emitas, le aplicararas un map, ese ultimo map que mencione volvera esa lista de
        //entities a una lista de models
        //en pocas palabras el primer map esta agarrando cada elememento de una lista
        //donde cada elemento es una lista de entities, y a cada lista de entities la estamos volviendo una lista de models
        //finalmente tendriamos una lista de lista de models o Flow<List<Lodging>>
    }

    suspend fun findById(id: Long): Lodging? {
        return lodgingDao.findById(id)?.toModel()

    }

    suspend fun upsertLodging(lodging: Lodging): Long {
        return lodgingDao.upsert(lodging.toEntity())
        //esto retorna un long ya que retornamos el id de lo que acabamos de actualizar
    }

    suspend fun upsertAllLodgings(lodgings: List<Lodging>) {
        val entities = lodgings.map { it.toEntity() }
        lodgingDao.upsertAll(entities)
    }

}
