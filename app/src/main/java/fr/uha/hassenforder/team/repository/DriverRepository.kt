package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.team.database.DriverDao
import fr.uha.hassenforder.team.model.Driver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DriverRepository(
    private val dispatcher: CoroutineDispatcher,
    private val driverDao : DriverDao
) {
    fun getAll () : Flow<List<Driver>> {
        return driverDao.getAll()
    }

    fun getDriverById (id : Long) : Flow<Driver?> {
        return driverDao.getDriverById(id)
    }

    @WorkerThread
    suspend fun create(driver: Driver) : Long = withContext(dispatcher){
        return@withContext driverDao.create(driver)
    }

    @WorkerThread
    suspend fun update(driver: Driver) : Long = withContext(dispatcher){
        return@withContext driverDao.update(driver)
    }

    @WorkerThread
    suspend fun upsert(driver: Driver) : Long = withContext(dispatcher){
        return@withContext driverDao.upsert(driver)
    }

    @WorkerThread
    suspend fun delete(driver: Driver) = withContext(dispatcher){
        driverDao.delete(driver)
    }
}