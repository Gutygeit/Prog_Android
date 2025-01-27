package fr.uha.hassenforder.team.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.hassenforder.android.database.DatabaseTypeConverters
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation
import fr.uha.hassenforder.team.model.ShiftVehiculeAssociation
import fr.uha.hassenforder.team.model.Vehicle

@TypeConverters(DatabaseTypeConverters::class)
@Database(
    entities = [
        Vehicle::class,
        Driver::class,
        Shift::class,
        ShiftVehiculeAssociation::class,
        ShiftDriverAssociation::class
    ],
    version = 3,
    exportSchema = false
)

abstract class ShiftDatabase : RoomDatabase() {

    abstract fun vehicleDAO() : VehicleDao

    abstract fun driverDAO() : DriverDao

    abstract fun shiftDAO() : ShiftDao

    companion object {
        private lateinit var instance : ShiftDatabase

        fun create (context : Context) : ShiftDatabase {
            instance = Room.databaseBuilder(context, ShiftDatabase::class.java, "shift.db")
                .fallbackToDestructiveMigration()
                .build()
            return instance
        }

        fun get() : ShiftDatabase {
            return instance
        }

    }
}