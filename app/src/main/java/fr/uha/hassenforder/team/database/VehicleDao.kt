package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * from vehicles")
    fun getAll () : Flow<List<Vehicle>>

    @Query("SELECT * from vehicles WHERE vid = :id")
    fun getVehicleById (id : Long) : Flow<Vehicle?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (vehicle: Vehicle) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update (vehicle: Vehicle) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (vehicle: Vehicle) : Long

    @Delete
    suspend fun delete (vehicle: Vehicle)
}