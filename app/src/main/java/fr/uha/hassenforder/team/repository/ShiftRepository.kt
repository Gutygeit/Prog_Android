package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.team.database.ShiftDao
import fr.uha.hassenforder.team.database.ShiftUpdateDTO
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.FullShift
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ShiftRepository(
    private val dispatcher: CoroutineDispatcher,
    private val shiftDao : ShiftDao
) {

    fun getAll () : Flow<List<Shift>> {
        return shiftDao.getAll()
    }

    fun getShiftById (id : Long) : Flow<FullShift?> {
        return shiftDao.getShiftById(id)
    }

    @WorkerThread
    suspend fun create(shift: Shift) : Long = withContext(dispatcher){
        return@withContext shiftDao.create(shift)
    }

    @WorkerThread
    suspend fun update(update: ShiftUpdateDTO) = withContext(dispatcher){
        when (update) {
            is ShiftUpdateDTO.Location -> shiftDao.update(update)
            is ShiftUpdateDTO.Date -> shiftDao.update(update)
            is ShiftUpdateDTO.Duration -> shiftDao.update(update)
            is ShiftUpdateDTO.Vehicle -> shiftDao.update(update)
            is ShiftUpdateDTO.Driver -> shiftDao.update(update)
        }
    }

    @WorkerThread
    suspend fun upsert(shift: Shift) : Long = withContext(dispatcher){
        return@withContext shiftDao.upsert(shift)
    }

    @WorkerThread
    suspend fun delete(shift: Shift) = withContext(dispatcher){
        shiftDao.delete(shift)
    }

    @WorkerThread
    suspend fun addMember(sid : Long, driver : Driver) = withContext(dispatcher){
        shiftDao.addMember(ShiftDriverAssociation(sid, driver.did))
    }

    @WorkerThread
    suspend fun removeMember(sid : Long, driver : Driver) = withContext(dispatcher){
        shiftDao.deleteMember(ShiftDriverAssociation(sid, driver.did))
    }
}

