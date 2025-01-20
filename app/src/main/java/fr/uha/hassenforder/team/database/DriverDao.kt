package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Driver
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Query("SELECT * from drivers")
    fun getAll () : Flow<List<Driver>>

    @Query("SELECT * from drivers WHERE did = :id")
    fun getDriverById (id : Long) : Flow<Driver?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (driver: Driver) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update (driver: Driver) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (driver: Driver) : Long

    @Delete
    suspend fun delete (driver: Driver)
}