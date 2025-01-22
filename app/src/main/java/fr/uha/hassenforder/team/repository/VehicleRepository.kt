package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.team.database.VehicleDao
import fr.uha.hassenforder.team.model.Vehicle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VehicleRepository (
    private val dispatcher: CoroutineDispatcher,
    private val vehicleDao : VehicleDao
){
    fun getAll () : Flow<List<Vehicle>> {
        return vehicleDao.getAll()
    }

    fun getVehicleById (id : Long) : Flow<Vehicle?> {
        return vehicleDao.getVehicleById(id)
    }

    @WorkerThread
    suspend fun create(vehicle: Vehicle) : Long = withContext(dispatcher){
        return@withContext vehicleDao.create(vehicle)
    }

    @WorkerThread
    suspend fun update(vehicle: Vehicle) : Long = withContext(dispatcher){
        return@withContext vehicleDao.update(vehicle)
    }

    @WorkerThread
    suspend fun upsert(vehicle: Vehicle) : Long = withContext(dispatcher){
        return@withContext vehicleDao.upsert(vehicle)
    }

    @WorkerThread
    suspend fun delete(vehicle: Vehicle) = withContext(dispatcher){
        vehicleDao.delete(vehicle)
    }
}