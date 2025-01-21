package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.uha.hassenforder.team.model.FullShift
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    @Query("SELECT * from shifts")
    fun getAll () : Flow<List<Shift>>

    @Query("SELECT * from shifts WHERE sid = :id")
    fun getShiftById (id : Long) : Flow<FullShift?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (team: Shift) : Long

    @Update(entity = Shift::class)
    suspend fun update (team : ShiftUpdateDTO.Location)

    @Update(entity = Shift::class)
    suspend fun update (team : ShiftUpdateDTO.Date)

    @Update(entity = Shift::class)
    suspend fun update (team : ShiftUpdateDTO.Duration)

    @Update(entity = Shift::class)
    suspend fun update (team : ShiftUpdateDTO.Driver)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (team: Shift) : Long

    @Delete
    suspend fun delete (team: Shift)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMember (assoc : ShiftDriverAssociation)

    @Delete
    suspend fun deleteMember (assoc : ShiftDriverAssociation)

}